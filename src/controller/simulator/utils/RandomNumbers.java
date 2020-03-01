package controller.simulator.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import simulator.workload.generator.configuration.Dist;
import simulator.workload.generator.configuration.PeriodicValidValues;
import simulator.workload.generator.configuration.types.ParameterAttributesDistributionType;
import cern.jet.random.AbstractDistribution;
import cern.jet.random.Exponential;
import cern.jet.random.Gamma;
import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import controller.simulator.utils.random.MinMaxDistribution;
import eduni.simjava.distributions.AbstractSpecificGenerator;
import eduni.simjava.distributions.DetailDistGenerator;
import eduni.simjava.distributions.GeneralGenerator;
import eduni.simjava.distributions.PeriodicGenerator;

/**
 * A class that stores random number generator for given metrics. Its functionality is basically similar to a hash table
 * where a key is a metric name and a value stored at that key is a previously set random number generator with proper distribution
 */
public class RandomNumbers {

	/**
	 * a hash table that stores the random number generators on the basis of the given key
	 * the key is the name of the parameter (metric name, String object), for which a random generator should be retrieved
	 */
	protected Map<String, List<AbstractSpecificGenerator>> rndGenerators;  

	/**
	 * holds mapping between node id and internal random numbers generator name.
	 */
	protected HashMap<String, String> idMap;
	
	/**
	 * A field indicating, that if no seed is given to a value, this global seed is to be used. 
	 * If this value is <code>null</code>, then a random number is to be picked (e.g. System.nanoTime()).
	 * Such functionality is used for example for test purposes.
	 */
	protected static Long baseSeed;
	
	/**
	 * Default constructor
	 */
	public RandomNumbers() {
		rndGenerators = new HashMap<String, List<AbstractSpecificGenerator>>();	
		idMap = new HashMap<String, String>();
		init();
	}
	
	/**
	 * Constructor with initial capacity
	 * @param initialCapacity the initial capacity of this hash map
	 */
	public RandomNumbers(int initialCapacity) {
		rndGenerators = new HashMap<String, List<AbstractSpecificGenerator>>(initialCapacity);
		idMap = new HashMap<String, String>(initialCapacity);
		init();
	}
	
	/** A common initialization method used in constructors */
	protected void init() {
	}
	
	/**
	 * Adds a random number generator at given key (name) 
	 * @param parameterName the name of the generator
	 * @param generator the generator to be stored
	 */
	public void addRandomGenerator(String parameterName, PeriodicGenerator generator) {
		List<AbstractSpecificGenerator> list;
		
		if(rndGenerators.containsKey(parameterName)){
			list = rndGenerators.get(parameterName);
			list.add(generator);
		} else {
			list = new ArrayList<AbstractSpecificGenerator>(1);
			list.add(generator);
			rndGenerators.put(parameterName, list);
		}
	}
	
	public void addRandomGenerator(String parameterName, DetailDistGenerator generator){
		ArrayList <AbstractSpecificGenerator>list;
		int idx = -1;
		if(rndGenerators.containsKey(parameterName)){
			list = (ArrayList<AbstractSpecificGenerator>) rndGenerators.get(parameterName);
			list.ensureCapacity(list.size() + 1);

			// this should guarantee, that elements on the list are ordered.
			while(++idx < list.size() && generator.compareTo(list.get(idx)) > 0);
			list.add(idx, generator);
			
		} else {
			list = new ArrayList<AbstractSpecificGenerator>(1);
			list.add(generator);
			rndGenerators.put(parameterName, list);
		}
	}
	
	public boolean addRandomGenerator(String parameterName, Dist detailDist){
		Double percnt = Double.valueOf(detailDist.getContent());
		
		AbstractDistribution generator = createRandomGenerator(
				detailDist.getDistribution().getType(), detailDist.hasAvg(), detailDist.getAvg(),
				detailDist.getStdev(), detailDist.hasMin(), detailDist.getMin(),
				detailDist.hasMax(), detailDist.getMax(), detailDist.hasSeed(), detailDist.getSeed(), parameterName);
		
		if(generator == null)
			return false;
		
		DetailDistGenerator ddg = new DetailDistGenerator(generator, percnt);
		addRandomGenerator(parameterName, ddg);
		
		return true;
	}
	
	public boolean addRandomGenerator(String parameterName,  PeriodicValidValues values){
		AbstractDistribution generator = createRandomGenerator(
					values.getDistribution().getType(), values.hasAvg(), values.getAvg(),
					values.getStdev(), values.hasMin(), values.getMin(), 
					values.hasMax(), values.getMax(), values.hasSeed(), values.getSeed(), parameterName);
		
		if(generator == null)
			return false;
		
		PeriodicGenerator pg = new PeriodicGenerator(generator, values.getBeginValidTime(), values.getEndValidTime());
		addRandomGenerator(parameterName, pg);
		
		return true;
	}

	
	/**
	 * Creates and stores a random numbers generator
	 * @param distribution the distribution of this generator descried as in {@link ParameterAttributesDistributionType}
	 * @param avg the average value
	 * @param stdev the standard deviation value 
	 * @param min minimal value
	 * @param max maximal value
	 * @param useSeed true, if the given seed is to be used
	 * @param seed the seed for random number generator
	 * @param name the name of the random numbers generator
	 */
	public boolean addRandomGenerator(int distribution, boolean useAvg, double avg, double stdev, boolean useMin, double min, boolean useMax, double max, boolean useSeed, long seed, String name) {
		AbstractDistribution generator = createRandomGenerator(distribution, useAvg, avg, stdev, useMin, min, useMax, max, useSeed, seed, name);
		if(generator == null)
			return false;
		
		GeneralGenerator gg = new GeneralGenerator(generator);
		List<AbstractSpecificGenerator> list;
		
		if(rndGenerators.containsKey(name)){
			list = rndGenerators.get(name);
			list.add(gg);
		} else {
			list = new ArrayList<AbstractSpecificGenerator>(1);
			list.add(gg);
			rndGenerators.put(name, list);
		}
		
		return true;
	}
	
	
	
	/**
	 * Retrieves the next random value from the given random numbers generator.
	 * Generator is valid for whole simulation time.
	 * @param parameterName the random numbers generator name
	 * @return the next random value (according to the setting of the random numbers generator)
	 * @throws IllegalAccessException
	 */
	public double getRandomValue(String parameterName) throws IllegalAccessException{
		List<AbstractSpecificGenerator> list = rndGenerators.get(parameterName);
		AbstractSpecificGenerator asg;
		AbstractDistribution generator = null;
		double ret;
		
		if(list == null || list.size() == 0){
			throw new IllegalAccessException("No random generator for given metric " + parameterName);
		}
		
		// generator on index 0 should be valid for whole simulation period, if PeriodicGenerator is used or
		// should cover 100% of tasks if DetailDistGenerator is used
		asg = list.get(0);
		
		generator = asg.getGenerator();
		
		ret = generator.nextDouble();
		asg.setLastGeneratedValue(ret);
		
		return ret;
	}
	
	public double getLastGeneratedRandomValue(String parameterName) throws IllegalAccessException{
		List<AbstractSpecificGenerator> list = rndGenerators.get(parameterName);
		AbstractSpecificGenerator asg = null;
		for (int i = 0; i < list.size(); i++) {
			asg = list.get(i);
			if(asg.getLastGenerated() == true)
				break;
		}
		return asg.getLastGeneratedValue();
	}
	
	
	/**
	 * Retrieves the next random value from the given random numbers generator.
	 * Generator is valid only for some period of simulation time.
	 * @param parameterName the random numbers generator name
	 * @param time current simulation time
	 * @return the next random value (according to the setting of the random numbers generator)
	 * @throws IllegalAccessException
	 */
	public double getRandomValue(String parameterName, long time) throws IllegalAccessException {
		List<AbstractSpecificGenerator> list = rndGenerators.get(parameterName);
		AbstractSpecificGenerator asg = null;
		PeriodicGenerator pGen = null;
		AbstractDistribution generator = null;
		double ret;
		
		if(list == null || list.size() == 0) {
			throw new IllegalAccessException("No random generator for given metric " + parameterName);
		}
		
		for(int i = 1; i < list.size(); i++){
			asg = list.get(i);
			if(asg instanceof PeriodicGenerator)
				pGen = (PeriodicGenerator) asg;
			else
				throw new RuntimeException("Wrong specific generator type. PeriodicGenerator was expected.");
			
			if(pGen.isValid(time)){
				generator = pGen.getGenerator();
				break;
			}
		}
		
		// generator on index = 0 should be valid for whole simulation period
		if(generator == null) {
			pGen = (PeriodicGenerator)list.get(0);
			if(pGen.isValid(time)){
				generator = pGen.getGenerator();
			}
		}
		
		if(generator == null)
			throw new IllegalAccessException("No valid "+parameterName+" generator for given time " + time);
		ret = generator.nextDouble();
		
		return ret;
	}
	
	/**
	 * Retrieves the next random value from the given random numbers generator.
	 * Generator is valid for specified percent of generated tasks.
	 * @param parameterName
	 * @param prcnt
	 * @return
	 * @throws IllegalAccessError
	 */
	public double getRandomValue(String parameterName, double prcnt) throws IllegalAccessError {
		int idx = 0;
		double prcntSum = 0.0;
		DetailDistGenerator ddg = null;
		
		if(!rndGenerators.containsKey(parameterName))
			throw new IllegalAccessError("No generators for "+parameterName+" available.");
		
		List <AbstractSpecificGenerator> list = rndGenerators.get(parameterName);

		if(list.size() == 0)
			throw new IllegalAccessError("No generator for "+parameterName + " available.");
		
		if(list.get(0) instanceof DetailDistGenerator){
			/* 
			 * Looking for appropriate generator.
			 * List of elements is ordered from min to max. For loop is checking to which interval requested 
			 * percent belongs to.
			 */
			for(idx = 0; idx < list.size(); idx++){
				ddg = (DetailDistGenerator)list.get(idx);
				prcntSum = prcntSum + ddg.getPrcnt();
				// this is to handle double numbers representation precision
				if((prcntSum - prcnt) > 0.000000001) {
					ddg.setLastGenerated(true);
					break;
				} else {
					ddg.setLastGenerated(false);
				}
			}
			
			if(idx >= list.size())
				throw new IllegalAccessError("Value "+prcnt +" doeas not belong to any interval defined by DetailDistGenerators");

			return ddg.getGenerator().nextDouble();

		} else {
			throw new IllegalAccessError(list.get(0).getClass().getName() +
					" generator can not be used for evaluating values for multidistribution element definition.\n" +
					"Use " + DetailDistGenerator.class.getName() + " instead.");
		}
	}
	
	/**
	 * Retrieves a snapshot of all random numbers generators next random values
	 * @return a result hash map has structure of pairs as: &lt;String random_numbers_generator_name, Double random_value&gt;
	 */
	public Map<String, Double> getMappedRandomValues() {
		Map<String, Double> resultHashMap = new HashMap<String, Double>(rndGenerators.size());
		
		Set<String> keys = rndGenerators.keySet();
		for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
			String name = i.next();
			Double value = null;
			try {
				value = new Double(getRandomValue(name));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			resultHashMap.put(name, value);
		}
		
		return resultHashMap;
	}

	/**
	 * Constructs a new random numbers generator.
	 * 
	 * @param distribution the distribution of this generator descried as in {@link ParameterAttributesDistributionType}
	 * @param useAvg <code>true</code>, if the given average value is to be used; <code>false</code> otherwise
	 * @param avg the average value
	 * @param stdev the standard deviation value 
	 * @param useMin <code>true</code>, if the given minimal value is to be used; <code>false</code> otherwise
	 * @param min minimal value
	 * @param useMax <code>true</code>, if the given maximal value is to be used; <code>false</code> otherwise
	 * @param max maximal value
	 * @param useSeed <code>true</code>, if the given seed is to be used; <code>false</code> otherwise
	 * @param seed the seed for random number generator
	 * @param name the name of the random numbers generator
	 * @return the constructed random numbers generator
	 */
	public AbstractDistribution createRandomGenerator(int distribution, boolean useAvg, double avg, double stdev, boolean useMin, double min, boolean useMax, double max, boolean useSeed, long seed, String name) {
		AbstractDistribution resultGenerator = null;
		
		if(useAvg == false && useMax == false && useMin == false)
			return null;
		
		int validSeed = -1;
		if (isBaseSeedSet()) { //use the base seed (has the highest priority)
			validSeed = baseSeed.intValue();
		} else if (useSeed) {
			validSeed = (int) seed;
		} else { //don't use seed
			validSeed = (int) System.nanoTime();
		}
		
		RandomEngine randomEngine = new MersenneTwister64(validSeed);
		
		switch (distribution) {
		case ParameterAttributesDistributionType.CONSTANT_TYPE:
			//this will result in a generator of constant values equal to avg
			resultGenerator = new Normal(avg, 0.0, randomEngine); 
			break;

		case ParameterAttributesDistributionType.NORMAL_TYPE:
			resultGenerator = new Normal(avg, stdev, randomEngine);
			break;

		case ParameterAttributesDistributionType.POISSON_TYPE:
			resultGenerator = new Poisson(avg, randomEngine);
			break;

		case ParameterAttributesDistributionType.UNIFORM_TYPE:
			resultGenerator = new Uniform(min, max, randomEngine);
			break;
			
		case ParameterAttributesDistributionType.EXPONENTIAL_TYPE:
			resultGenerator = new Exponential(avg, randomEngine);
			break;
			
		case ParameterAttributesDistributionType.GAMMA_TYPE:
			resultGenerator = new Gamma(avg, stdev, randomEngine);
			break;

		case ParameterAttributesDistributionType.HARMONIC_TYPE:
			resultGenerator = null; //TODO finish implementation
			break;
		}
		Double minValue = null;
		Double maxValue = null;
		boolean wrap = false; //indicates, whether the wrapper should be used
		if (useMin) {
			minValue = min;
			wrap = true;
		}
		if (useMax) {
			maxValue = max;
			wrap = true;
		}
		if (wrap) //wrap the generator, only if necessary
			resultGenerator = new MinMaxDistribution(resultGenerator, minValue, maxValue);
		return resultGenerator;
	}
	
	public void addIdGeneratorNameMapping(String id, String generatorName){
		idMap.put(id, generatorName);
	}
	
	public boolean containsElement(String id){
		return idMap.containsKey(id);
	}
	
	public String getGeneratorName(String id){
		return idMap.get(id);
	}
	
	public List<AbstractSpecificGenerator> getGenList(String arg){
		return rndGenerators.get(arg);
	}
	
	/**
	 * Sets a new value of the base seed for this class. If a <code>null</code> value
	 * is given, then the base seed is disabled.
	 * @param baseSeed the new value of the base seed, or <code>null</code> if the base seed is to be disabled
	 */
	public static void setBaseSeed(Long baseSeed) {
		RandomNumbers.baseSeed = baseSeed;
	}
	
	public static boolean isBaseSeedSet() {
		return (baseSeed != null);
	}
	
	public static long getBaseSeed() {
		return baseSeed;
	}
	
	public static void clearBaseSeed() {
		baseSeed = null;
	}
}
