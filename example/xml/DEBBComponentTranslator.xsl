<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:plm="http://www.plmxml.org/Schemas/PLMXMLSchema"
	xmlns:cim="http://www.coolemall.eu/DEBBComponent" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<!-- Specification of the output document -->
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<!-- Information for mapping of DEBB classes into DCWoRMS computingResource 
		classes -->
	<xsl:param name="computingResourceClasses" select="computingResourceClasses" />

	<!-- Information for mapping of resource classes into DCWoRMS resourceUnits 
		classes -->
	<xsl:param name="resourceUnitClasses" select="resourceUnitClasses" />

	<xsl:template match="/">
		<xsl:element name="environment">
			<xsl:attribute name="xsi:noNamespaceSchemaLocation">DCWormsResSchema.xsd</xsl:attribute>
			<xsl:element name="resources">
				<xsl:element name="scheduler"></xsl:element>
				<xsl:apply-templates select="//*[ComponentId]"
					mode="full" />
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<!-- TODO: Move most of "code" to basic template -->
	<xsl:template match="//*[ComponentId]" mode="full">
		<xsl:element name="computingResource">
			<xsl:attribute name="class"> 
		 		<xsl:value-of select="name(.)" /> 
		 	</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:variable name="oldComponentId"><xsl:value-of select="ComponentId" /></xsl:variable>
				<xsl:variable name="newComponentId">
				<xsl:value-of select="translate($oldComponentId, '/', '_')" />
				</xsl:variable>
				<xsl:value-of select="$newComponentId" />
			</xsl:attribute>
			<xsl:call-template name="translateComputingElement" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="//*[ComponentId]">
		<xsl:call-template name="translateComputingElement" />
	</xsl:template>

	<xsl:template name="translateComputingElement">
		<xsl:if test="Memory">
			<xsl:apply-templates select="Memory" />
		</xsl:if>
		<xsl:if test="Storage">
			<xsl:apply-templates select="Storage" />
		</xsl:if>
		<xsl:call-template name="BasicDEBBPhysicalElementParameters" />
		<xsl:if test="Baseboard">
			<xsl:apply-templates select="Baseboard" />
		</xsl:if>
		<xsl:if test="Sensor">
			<xsl:apply-templates select="Sensor" />
		</xsl:if>
		<xsl:if test="PowerSupply">
			<xsl:apply-templates select="PowerSupply" />
		</xsl:if>
		<xsl:if test="Processor">
			<xsl:apply-templates select="Processor" />
		</xsl:if>
		<xsl:if test="CoolingDevice">
			<!-- Base cooling device data -->
			<xsl:apply-templates select="CoolingDevice" mode="basic" />
			<!-- Air throughput connected, advanced, technical data -->
			<xsl:if test="CoolingDevice/ThroughputProfile">
				<xsl:element name="profile">
					<xsl:element name="airThroughputProfile">
						<xsl:element name="airThroughputStates">
							<xsl:apply-templates select="CoolingDevice"
								mode="advanced" />
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
		</xsl:if>
		
		<xsl:if test="Heatsink">
			<xsl:apply-templates select="Heatsink" />
		</xsl:if>	
		
		<xsl:if test="Outlet">
			<xsl:apply-templates select="Outlet" />
		</xsl:if>	

		<xsl:if test="Slot">
			<xsl:apply-templates select="Slot" />
		</xsl:if>	

	</xsl:template>

	<xsl:template match="Baseboard">
		<xsl:element name="parameter">
			<xsl:attribute name="name">baseboard</xsl:attribute>
			<!-- Don't call BasicDEBBPhysicalElementParameters, because parameter 
				cannot contain parameters. Must be parameter containing properties. -->
			<xsl:call-template name="BasicDEBBPhysicalElementProperties" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="Sensor">
		<xsl:element name="parameter">
			<xsl:attribute name="name">sensor</xsl:attribute>
			<xsl:element name="property">
				<xsl:attribute name="name">class</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Class" />
				</xsl:element>
			</xsl:element>
			<xsl:element name="property">
				<xsl:attribute name="name">unit</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Unit" />
				</xsl:element>
			</xsl:element>
			<xsl:if test="MinValue">
				<xsl:element name="property">
					<xsl:attribute name="name">minValue</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="MinValue" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:if test="MaxValue">
				<xsl:element name="property">
					<xsl:attribute name="name">maxValue</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="MaxValue" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:if test="Factor">
				<xsl:element name="property">
					<xsl:attribute name="name">factor</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="Factor" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:if test="Accuracy">
				<xsl:element name="property">
					<xsl:attribute name="name">accuracy</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="Accuracy" />
					</xsl:element>
				</xsl:element>
			</xsl:if>

			<!-- Don't call BasicDEBBPhysicalElementParameters, because parameter 
				cannot contain parameters. Must be parameter containing properties. -->
			<xsl:call-template name="BasicDEBBPhysicalElementProperties" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="Processor">
		<xsl:element name="computingResource">
			<xsl:attribute name="class">Processor</xsl:attribute>
			<xsl:call-template name="BasicDEBBPhysicalElementParameters" />
			<xsl:element name="parameter">
				<xsl:attribute name="name">speed</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="MaxClockSpeed" />
				</xsl:element>
			</xsl:element>
			<xsl:if test="PState | CState">
				<!-- If PState or CState elements present add powerProfile element -->
				<xsl:element name="profile">
					<xsl:element name="powerProfile">
						<xsl:if test="PState/Transition">
							<xsl:element name="powerStates">
								<!-- <xsl:element name="powerState"> -->
								<!-- TODO: What to take if from??? -->
								<!-- <xsl:element name="name">DummyName</xsl:element> -->
								<!-- <xsl:element name="powerUsage">0</xsl:element> -->
								<xsl:apply-templates select="PState/Transition" />
								<!-- </xsl:element> -->
							</xsl:element>
						</xsl:if>
						<xsl:apply-templates select="PState" />
						<xsl:apply-templates select="CState" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>

	<xsl:template match="PState">
		<xsl:element name="parameter">
			<xsl:attribute name="name">pState</xsl:attribute>
			<xsl:element name="property">
				<xsl:attribute name="name">name</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="State" />
				</xsl:element>
			</xsl:element>
			<xsl:element name="property">
				<xsl:attribute name="name">frequency</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Frequency" />
				</xsl:element>
			</xsl:element>
			<xsl:element name="property">
				<xsl:attribute name="name">voltage</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Voltage" />
				</xsl:element>
			</xsl:element>
			<xsl:element name="property">
				<xsl:attribute name="name">powerUsage</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="PowerUsage" />
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template match="CState">
		<xsl:element name="parameter">
			<xsl:attribute name="name">pState</xsl:attribute>
			<xsl:element name="property">
				<xsl:attribute name="name">name</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="State" />
				</xsl:element>
			</xsl:element>
			<xsl:element name="property">
				<xsl:attribute name="name">powerUsage</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="PowerUsage" />
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template match="PState/Transition">
		<xsl:element name="powerState">
			<xsl:element name="name">
				<xsl:value-of select="../State" />
			</xsl:element>
			<xsl:element name="transition">
				<xsl:element name="to">
					<xsl:value-of select="ToState" />
				</xsl:element>
				<xsl:element name="powerUsage">
					<xsl:value-of select="PowerUsage" />
				</xsl:element>
				<xsl:element name="time">
					<xsl:value-of select="Time" />
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template name="BasicStateParameters">
		<xsl:element name="name">
			<xsl:value-of select="State" />
		</xsl:element>
		<xsl:element name="powerUsage">
			<xsl:value-of select="PowerUsage" />
		</xsl:element>
		<xsl:element name="transition">
			<xsl:element name="to">
				<xsl:value-of select="Transition/ToState" />
			</xsl:element>
			<xsl:element name="powerUsage">
				<xsl:value-of select="Transition/PowerUsage" />
			</xsl:element>
			<xsl:element name="time">
				<xsl:value-of select="Transition/Time" />
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template match="PowerSupply">
		<xsl:element name="parameter">
			<xsl:attribute name="name">powerSupply</xsl:attribute>
			<!-- Don't call BasicDEBBPhysicalElementParameters, because parameter 
				cannot contain parameters. Must be parameter containing properties. -->
			<xsl:call-template name="BasicDEBBPhysicalElementProperties" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="Memory">
		<xsl:element name="resourceUnit">
			<xsl:attribute name="class">Memory</xsl:attribute>
			<xsl:element name="amount">
				<xsl:value-of select="Capacity" />
			</xsl:element>
			<xsl:call-template name="BasicDEBBPhysicalElementParameters" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="Storage">
		<xsl:element name="resourceUnit">
			<xsl:attribute name="class">Storage</xsl:attribute>
			<xsl:attribute name="type"><xsl:value-of select="Class" /></xsl:attribute>
			<xsl:element name="amount">
				<xsl:attribute name="unit">MB</xsl:attribute>
				<xsl:value-of select="Capacity" />
			</xsl:element>
			<xsl:element name="parameter">
				<xsl:attribute name="name">interface</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Interface" />
				</xsl:element>
			</xsl:element>
			<xsl:call-template name="BasicDEBBPhysicalElementParameters" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="CoolingDevice" mode="basic">
		<xsl:element name="parameter">
			<xsl:attribute name="name">CoolingDevice</xsl:attribute>
			<!-- Don't call BasicDEBBPhysicalElementParameters, because parameter 
				cannot contain parameters. Must be parameter containing properties. -->

			<!-- Obligatory attribute -->
			<xsl:element name="property">
				<xsl:attribute name="name">class</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Class" />
				</xsl:element>
			</xsl:element>
			<xsl:if test="MaxCoolingCapacity">
				<xsl:element name="property">
					<xsl:attribute name="name">maxCoolingCapacity</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="MaxCoolingCapacity" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:if test="MaxAirThroughput">
				<xsl:element name="property">
					<xsl:attribute name="name">maxAirThroughput</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="MaxAirThroughput" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:if test="MaxWaterThroughput">
				<xsl:element name="property">
					<xsl:attribute name="name">maxWaterThroughput</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="MaxWaterThroughput" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:call-template name="BasicDEBBPhysicalElementProperties" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="CoolingDevice" mode="advanced">
		<xsl:attribute name="name"><xsl:value-of select="generate-id()" /></xsl:attribute>
		<xsl:if test="ThroughputProfile">
			<xsl:apply-templates select="ThroughputProfile" />
		</xsl:if>
	</xsl:template>

	<xsl:template match="ThroughputProfile">
		<xsl:apply-templates select="FlowState" />
	</xsl:template>

	<xsl:template match="FlowState">
		<xsl:element name="airThroughputState">
			<xsl:element name="name">
				<xsl:value-of select="State" />
			</xsl:element>
			<xsl:if test="Flow">
				<xsl:element name="value">
					<xsl:value-of select="Flow" />
				</xsl:element>
			</xsl:if>
			<xsl:if test="PowerUsage">
				<xsl:element name="powerUsage">
					<xsl:value-of select="PowerUsage" />
				</xsl:element>
			</xsl:if>
			<xsl:if test="Transition">
				<xsl:apply-templates select="Transition" />
			</xsl:if>
		</xsl:element>
	</xsl:template>

	<xsl:template match="Transition">
		<xsl:element name="parameter">
			<xsl:attribute name="name">toState</xsl:attribute>
			<xsl:element name="value">
				<xsl:value-of select="ToState" />
			</xsl:element>
		</xsl:element>
		<xsl:element name="parameter">
			<xsl:attribute name="name">powerUsage</xsl:attribute>
			<xsl:element name="value">
				<xsl:value-of select="PowerUsage" />
			</xsl:element>
		</xsl:element>
		<xsl:element name="parameter">
			<xsl:attribute name="name">time</xsl:attribute>
			<xsl:element name="value">
				<xsl:value-of select="Time" />
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<!-- Template for transforming basic parameters of all devices of any type -->
	<xsl:template name="BasicDEBBPhysicalElementParameters">
		<xsl:if test="Manufacturer">
			<xsl:element name="parameter">
				<xsl:attribute name="name">manufacturer</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Manufacturer" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if test="Product">
			<xsl:element name="parameter">
				<xsl:attribute name="name">product</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Product" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if test="MaxPower">
			<xsl:element name="parameter">
				<xsl:attribute name="name">maxPower</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="MaxPower" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
	</xsl:template>

	<xsl:template name="BasicDEBBSimpleTypeProperties">
		<xsl:comment>BasicDEBBSimpleTypeProperties</xsl:comment>
		
		<xsl:call-template name="BasicDEBBPhysicalElementProperties"/>
		<!-- Transform (not needed in DCWoRMS) -->
<!--		<xsl:if test="Transform">-->
<!--			<xsl:element name="property">-->
<!--				<xsl:attribute name="name">transform</xsl:attribute>-->
<!--				<xsl:element name="value">-->
<!--					<xsl:value-of select="Transform" />-->
<!--				</xsl:element>-->
<!--			</xsl:element>		-->
<!--		</xsl:if>-->
		
		<!-- Reference (not needed in DCWoRMS) -->
<!--		<xsl:if test="Reference">-->
<!--			<xsl:element name="property">-->
<!--				<xsl:attribute name="name">reference</xsl:attribute>-->
<!--				<xsl:element name="value">-->
<!--					<xsl:value-of select="Reference" />-->
<!--				</xsl:element>-->
<!--			</xsl:element>		-->
<!--		</xsl:if>			-->
	</xsl:template>
	
	<xsl:template name="BasicDEBBPhysicalElementProperties">
		<xsl:comment>BasicDEBBPhysicalElementProperties</xsl:comment>
		
		<xsl:if test="Label">
			<xsl:element name="property">
				<xsl:attribute name="name">label</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Label" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		
		<xsl:if test="Manufacturer">
			<xsl:element name="property">
				<xsl:attribute name="name">manufacturer</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Manufacturer" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		
		<xsl:if test="Product">
			<xsl:element name="property">
				<xsl:attribute name="name">product</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Product" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		
		<xsl:if test="MaxPower">
			<xsl:element name="property">
				<xsl:attribute name="name">maxPower</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="MaxPower" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		
		<xsl:if test="PowerUsage">
			<xsl:element name="property">
				<xsl:attribute name="name">powerUsage</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="PowerUsage" />
				</xsl:element>
			</xsl:element>
		</xsl:if>

		<!-- PowerUsageProfile -->
		<!-- TODO: It should be translated, but it is not clear how. Impossible
		 to map hierarchy in parameters and their properties, which is necessary here. -->
<!--		<xsl:if test="PowerUsageProfile">-->
<!--			<xsl:element name="property">-->
<!--				<xsl:attribute name="name">powerUsageProfile</xsl:attribute>-->
<!--				<xsl:element name="value">-->
<!--					<xsl:value-of select="PowerUsageProfile" />-->
<!--				</xsl:element>-->
<!--			</xsl:element>-->
<!--		</xsl:if>-->

		
		<xsl:if test="Type">
			<xsl:element name="property">
				<xsl:attribute name="name">type</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Type" />
				</xsl:element>
			</xsl:element>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="Heatsink">
		<xsl:element name="parameter">
			<xsl:attribute name="name">heatsink</xsl:attribute>
			
			<xsl:if test="TransferRate">
				<xsl:element name="property">
					<xsl:attribute name="name">transferRate</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="TransferRate" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			
			<!-- Don't call BasicDEBBPhysicalElementParameters, because parameter 
				cannot contain parameters. Must be parameter containing properties. -->
			<xsl:call-template name="BasicDEBBSimpleTypeProperties" />
		</xsl:element>	
	</xsl:template>	
	
	<xsl:template match="Outlet">
		<xsl:element name="parameter">
			<xsl:attribute name="name">outlet</xsl:attribute>
			
			<xsl:if test="MaxRPM">
				<xsl:element name="property">
					<xsl:attribute name="name">maxRPM</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="MaxRPM" />
					</xsl:element>
				</xsl:element>
			</xsl:if>
			
			<!-- Don't call BasicDEBBPhysicalElementParameters, because parameter 
				cannot contain parameters. Must be parameter containing properties. -->
			<xsl:call-template name="BasicDEBBSimpleTypeProperties" />
		</xsl:element>
	
	</xsl:template>	
	
	<xsl:template match="Slot">
		<xsl:element name="parameter">
			<xsl:attribute name="name">slot</xsl:attribute>	
			
			<xsl:if test="Number">
				<xsl:element name="property">
					<xsl:attribute name="name">number</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="Number" />
					</xsl:element>
				</xsl:element>
			</xsl:if>

			<xsl:element name="property">
				<xsl:attribute name="name">connectorType</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="ConnectorType" />
				</xsl:element>
			</xsl:element>

			<xsl:element name="property">
				<xsl:attribute name="name">label</xsl:attribute>
				<xsl:element name="value">
					<xsl:value-of select="Label" />
				</xsl:element>
			</xsl:element>
			
			<!-- Transform ignored -->
			
			<xsl:if test="AvailSpace">
				<xsl:element name="property">
					<xsl:attribute name="name">availSpace</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="AvailSpace" />
					</xsl:element>
				</xsl:element>
			</xsl:if>			
		</xsl:element>	
	</xsl:template>
</xsl:stylesheet>