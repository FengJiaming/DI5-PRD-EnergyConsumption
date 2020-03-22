<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dcworms="DCWormsResSchema.xsd"
	xmlns:debb2dcworms="../../debb2dcworms.xsd"
	xmlns:fun="http://whatever">

	<!-- Specification of the output document -->
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<!-- Root of the DCWoRMS XML document -->
	<xsl:variable name="rootDoc" select="/" />

	<!-- Information about schedulers and estimators -->
	<xsl:param name="additionalInformationFileName" select="additionalInformationFileName" />


	<xsl:variable name="nestedDoc"
		select="document($additionalInformationFileName)" />

	<xsl:template match="/">
		<xsl:comment>
			This is DEBB information (taken from PLM XML and DEBBComponent
			files), updated by schedulers and estimators. Moreover, all elements
			are sorted and appear in correct order, according to the schema.
		</xsl:comment>
		<xsl:comment>
			Schedulers and estimators information is loaded from
			<xsl:value-of select="$additionalInformationFileName" />
			file
		</xsl:comment>

		<xsl:apply-templates select="environment" />

	</xsl:template>

	<xsl:template match="environment">
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates select="$nestedDoc//timeEstimationPlugin" />
			<xsl:apply-templates select="resources" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="resources">
		<xsl:copy copy-namespaces="no">
			<xsl:apply-templates select="//resources/computingResource" />
			<xsl:element name="scheduler">
				<xsl:attribute name="class">Cluster</xsl:attribute>
				<xsl:attribute name="name">cluster</xsl:attribute>
				<!-- In first version, for simplicity, we assume we have only one scheduler 
					and one scheduling plugin. If $additionalInformationFileName contains more 
					than one, the rest is ignored -->
				<xsl:apply-templates select="$nestedDoc//schedulingPlugin[1]" />
			</xsl:element>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="timeEstimationPlugin">
		<xsl:element name="timeEstimationPlugin">
			<xsl:element name="name">
				<xsl:value-of select="name" />
			</xsl:element>
		</xsl:element>
	</xsl:template>


	<xsl:template match="schedulingPlugin">
		<xsl:variable name="pluginName" select="name" />
		<xsl:element name="schedulingPlugin">
			<xsl:element name="name">
				<xsl:value-of select="$pluginName" />
			</xsl:element>
			<xsl:if test="frequency != ''">
				<xsl:element name="frequency">
					<xsl:value-of select="frequency" />
				</xsl:element>
			</xsl:if>
		</xsl:element>
		<xsl:element name="managedComputingResources">
			<!-- TODO: This parameter should be taken from somewhere. -->
			<xsl:attribute name="include">false</xsl:attribute>

			<!-- Find appropriate resources (identified by class, type and name(s)) 
				and add their names here -->
			<xsl:variable name="resourceClass">
				<xsl:value-of select="resources/class" />
			</xsl:variable>
			<!-- <xsl:value-of select="$resourceClass"/> -->
			<xsl:variable name="resourceType">
				<xsl:value-of select="resources/type" />
			</xsl:variable>
			<!-- <xsl:value-of select="$resourceType"/> -->

			<!-- Not clear why it does not work this way :/ -->
			<!-- <xsl:variable name="resourceNames"><xsl:value-of select="resources/name"/></xsl:variable> -->
			<xsl:variable name="resourceNames"
				select="$nestedDoc//schedulingPlugin[name=$pluginName]/resources/name" />

			<xsl:choose>
				<!-- Resource names are present -->
				<xsl:when test="count($resourceNames) > 0">
					<xsl:for-each select="$resourceNames">
						<xsl:variable name="index" select="position()" />
						<xsl:variable name="resourceName" select="." />
						<xsl:choose>
							<!-- Resource type is present -->
							<xsl:when test="$resourceType != ''">
								<xsl:apply-templates
									select="$rootDoc//computingResource[@class=$resourceClass and @type=$resourceType and @name=$resourceName]"
									mode="addResorceName" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates
									select="$rootDoc//computingResource[@class=$resourceClass and @name=$resourceName]"
									mode="addResorceName" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</xsl:when>
				<!-- No resource names -->
				<xsl:otherwise>
					<xsl:choose>
						<!-- Resource type is present -->
						<xsl:when test="$resourceType != ''">
							<xsl:apply-templates
								select="$rootDoc//computingResource[@class=$resourceClass and @type=$resourceType]"
								mode="addResorceName" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates
								select="$rootDoc//computingResource[@class=$resourceClass]"
								mode="addResorceName" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>

	<xsl:template match="energyEstimationPlugin">
		<xsl:element name="energyEstimationPlugin">
			<xsl:element name="name">
				<xsl:value-of select="name" />
			</xsl:element>
		</xsl:element>
	</xsl:template>

<!-- 	<xsl:template match="energyEstimationPlugin">
			<xsl:variable name="pluginName" select="name" />

			 Find appropriate resources (identified by class, type and name(s)) 
				and add their names here 
			<xsl:variable name="resourceClass">
				<xsl:value-of select="resources/class" />
			</xsl:variable>
			 <xsl:value-of select="$resourceClass"/> 
			<xsl:variable name="resourceType">
				<xsl:value-of select="resources/type" />
			</xsl:variable>
			 <xsl:value-of select="$resourceType"/> 

			 Not clear why it does not work this way :/ 
			 <xsl:variable name="resourceNames"><xsl:value-of select="resources/name"/></xsl:variable> 
			<xsl:variable name="resourceNames"
				select="$nestedDoc//energyEstimationPlugin[name=$pluginName]/resources/name" />

			<xsl:choose>
				 Resource names are present 
				<xsl:when test="count($resourceNames) > 0">
					<xsl:for-each select="$resourceNames">
						<xsl:variable name="index" select="position()" />
						<xsl:variable name="resourceName" select="." />
						<xsl:choose>
							 Resource type is present 
							<xsl:when test="$resourceType != ''">
								<xsl:apply-templates
									select="$rootDoc//computingResource[@class=$resourceClass and @type=$resourceType and @name=$resourceName]"
									mode="addEnergyEstimationPlugin" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates
									select="$rootDoc//computingResource[@class=$resourceClass and @name=$resourceName]"
									mode="addEnergyEstimationPlugin" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</xsl:when>
				 No resource names 
				<xsl:otherwise>
					<xsl:choose>
						 Resource type is present 
						<xsl:when test="$resourceType != ''">
							<xsl:apply-templates
								select="$rootDoc//computingResource[@class=$resourceClass and @type=$resourceType]"
								mode="addEnergyEstimationPlugin" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates
								select="$rootDoc//computingResource[@class=$resourceClass]"
								mode="addEnergyEstimationPlugin" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template> -->

	<xsl:template match="computingResource">
		<xsl:copy copy-namespaces="no">
			<xsl:copy-of select="@*" />
			<!-- Find nested elements in appropriate order and copy them -->
			<!-- resourceUnit -->
			<xsl:apply-templates select="resourceUnit" />
			<!-- parameter -->
			<xsl:apply-templates select="parameter" />
			
			<xsl:variable name="debbFullPath" select="fun:getFullPath(.)"/>
			<xsl:if test="$debbFullPath != ''">
				<xsl:element name="parameter">
					<xsl:attribute name="name">fullPath</xsl:attribute>
					<xsl:element name="value">
						<xsl:value-of select="$debbFullPath" />
					</xsl:element>
				</xsl:element>
			</xsl:if>				
			
			
			<xsl:variable name="resourceClass" select="@class"/>
			<xsl:variable name="resourceType" select="@type"/>
			<xsl:variable name="resourceName" select="@name"/>
			
			<xsl:variable name="estimationPluginName" select="fun:getEnergyEstimator($resourceClass, $resourceType, $resourceName)"/>
			<xsl:variable name="hasProfile" select="profile != ''"/>
			<xsl:variable name="hasPowerProfile" select="profile/powerProfile != ''"/>

			<xsl:if test="$hasProfile = false() and $estimationPluginName != ''">
				<xsl:element name="profile">
					<xsl:element name="powerProfile">
						<xsl:element name="energyEstimationPlugin">
							<xsl:element name="name">
								<xsl:value-of select="$estimationPluginName"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			
			<!-- profile -->
			<xsl:apply-templates select="profile" />
			<!-- location -->
			<xsl:apply-templates select="location" />
			<!-- computingResource -->
			<xsl:apply-templates select="computingResource" />
		</xsl:copy>
	</xsl:template>

	<xsl:function name="fun:getEnergyEstimator">
		<xsl:param name="computingResourceClass"/>
		<xsl:param name="computingResourceType"/>
		<xsl:param name="computingResourceName"/>
		
		<xsl:variable name="estimatorName" select="$nestedDoc//energyEstimationPlugin[(resources/class=$computingResourceClass and resources/type=$computingResourceType and resources/name=$computingResourceName) or
		(resources/class=$computingResourceClass and resources/type=$computingResourceType) or
		(resources/class=$computingResourceClass and resources/name=$computingResourceName) or
		resources/class=$computingResourceClass]/name"/>

		<xsl:value-of select="$estimatorName"/>
	</xsl:function>
	
	<xsl:template match="computingResource" mode="addResorceName">
		<xsl:element name="resourceName">
			<!-- <xsl:value-of select="../@name"></xsl:value-of> -->
			<xsl:value-of select="@name"></xsl:value-of>
		</xsl:element>
	</xsl:template>

	<xsl:template match="computingResource" mode="addEnergyEstimationPlugin">
		<xsl:element name="energyEstimationPlugin">
			<xsl:value-of select="name"></xsl:value-of>
		</xsl:element>
	</xsl:template>
	
	<xsl:function name="fun:getFullPath">
		<xsl:param name="currentComputingResource"/>
		<xsl:variable name="ancestors" select="$currentComputingResource/ancestor::computingResource/@name"/>
		
		<xsl:variable name="fullPath">
			<xsl:text>/</xsl:text>
			<xsl:for-each select="1 to count($ancestors)">
				<xsl:variable name="index" select="." />
				<xsl:value-of select="$ancestors[$index]"/>
				<xsl:text>/</xsl:text>
			</xsl:for-each>	
			<xsl:value-of select="$currentComputingResource/@name"></xsl:value-of>	
		</xsl:variable>
		
		<xsl:value-of select="$fullPath"/>
	</xsl:function>
	
	<xsl:template match="resourceUnit | parameter | location">
		<xsl:copy-of select="." copy-namespaces="no" />
	</xsl:template>

	<xsl:template match="profile">
		<xsl:copy copy-namespaces="no">
			<xsl:copy-of select="@*" />
			
			<xsl:apply-templates select="powerProfile" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="powerProfile">
		<xsl:copy copy-namespaces="no">
			<xsl:variable name="resourceClass" select="../../@class"></xsl:variable>
			<xsl:variable name="resourceType" select="../../@type"></xsl:variable>
			<xsl:variable name="resourceNames" select="../../@name"></xsl:variable>

			<xsl:choose>
				<!-- Resource names are present -->
				<xsl:when test="count($resourceNames) > 0">
					<xsl:for-each select="$resourceNames">
						<xsl:variable name="index" select="position()" />
						<xsl:variable name="resourceName" select="." />
						<xsl:choose>
							<!-- Resource type is present -->
							<xsl:when test="$resourceType != ''">
								<xsl:apply-templates
									select="$nestedDoc//energyEstimationPlugin[resources/class=$resourceClass and resources/type=$resourceType and resources/name=$resourceName]" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates
									select="$nestedDoc//energyEstimationPlugin[resources/class=$resourceClass and resources/name=$resourceName]" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</xsl:when>
				<!-- No resource names -->
				<xsl:otherwise>
					<xsl:choose>
						<!-- Resource type is present -->
						<xsl:when test="$resourceType != ''">
							<xsl:apply-templates
								select="$nestedDoc//energyEstimationPlugin[resources/class=$resourceClass and resources/type=$resourceType]" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates
								select="$nestedDoc//energyEstimationPlugin[resources/class=$resourceClass]" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			
			<!-- Copy all children of powerProfile -->
			<xsl:copy-of select="@*|node()" />
		</xsl:copy>

	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>