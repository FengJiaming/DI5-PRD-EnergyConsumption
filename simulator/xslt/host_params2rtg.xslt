<?xml version="1.0" encoding="UTF-8"?>

<!-- TO_DO:
ODTWORZYC LINKI
-->

<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
			     xmlns="http://www.QosCosGrid.org/resourceDescription"
			     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			     xmlns:set="http://exslt.org/sets" 
			     exclude-result-prefixes="set">
                             
	<xsl:strip-space elements="*"/>
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
	
	<xsl:variable name="AD_IDENTIFIER">AD_ID</xsl:variable>
	<xsl:variable name="RES_IDENTIFIER">RES_ID</xsl:variable>
	<xsl:variable name="GROUP_IDENTIFIER">GROUP_ID</xsl:variable>
	<xsl:variable name="GATEWAY">GATEWAY</xsl:variable>
	<xsl:variable name="GRID_DOMAIN_IDENTIFIER">GRID</xsl:variable>
	
	<xsl:key name="AD_RESOURCES" match="/hostParameters/resources/computingResource" use="additionalProperties/property[@name=$AD_IDENTIFIER]"/>
	<xsl:key name="AD_GROUPS" match="/hostParameters/topology/resourcesGroup" use="additionalProperties/property[@name=$AD_IDENTIFIER]"/>
	<xsl:variable name="DOMAINS" select="set:distinct(.//additionalProperties/property[@name=$AD_IDENTIFIER])"/>
	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="/hostParameters"/>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="hostParameters">
		<xsl:element name="rtg">
			<xsl:call-template name="CONTEXT_TEMPLATE"/>
			
			<xsl:call-template name="INTRA_AD_INFOS_TEMPLATE"/>
			
			<xsl:call-template name="GRID_LEVEL_TEMPLATE"/>
		</xsl:element>	 
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template name="CONTEXT_TEMPLATE">
		<xsl:element name="context">
			<xsl:attribute name="rtgType">GridTopologyRtg</xsl:attribute>
			<xsl:attribute name="isComplete">true</xsl:attribute>
						
			<xsl:if test="validity/timeSlot">
				<xsl:element name="defaultTimeInterval">
					<xsl:apply-templates select="validity/timeSlot[1]"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="timeSlot">
		<xsl:element name="lowerBoundedRange">
			<xsl:value-of select="slotStart"/>
		</xsl:element>
		<xsl:choose>
			<xsl:when test="slotEnd">
				<xsl:element name="upperBoundedRange">
					<xsl:value-of select="slotEnd"/>
				</xsl:element>
			</xsl:when>
			<xsl:when test="slotDuration">
				<xsl:element name="duration">
					<xsl:value-of select="slotDuration"/>
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="INTRA_AD_INFOS_TEMPLATE">
		<xsl:variable name="CURRENT" select="."/>
		<xsl:for-each select="$DOMAINS">
			<xsl:element name="intraADInfo">
				<xsl:attribute name="adId">
					<xsl:value-of select="."/>
				</xsl:attribute>
				<xsl:apply-templates select="key('AD_RESOURCES',.)" mode="TEMPLATE_MODE"/>
				<xsl:apply-templates select="key('AD_GROUPS',.)" mode="TEMPLATE_MODE"/>
				
				<xsl:apply-templates select="key('AD_RESOURCES',.)" mode="RESOURCE_MODE"/>
				<xsl:apply-templates select="key('AD_GROUPS',.)" mode="RESOURCE_MODE"/>
				
				<xsl:apply-templates select="key('AD_GROUPS',.)" mode="GROUP_MODE"/>
				
				<xsl:apply-templates select="key('AD_RESOURCES',.)" mode="AVAILABILITY_MODE"/>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="computingResource" mode="TEMPLATE_MODE">
		<xsl:element name="compResourceTemplate">
			<xsl:attribute name="id">
				<xsl:value-of select="additionalProperties/property[@name=$RES_IDENTIFIER]"/>
			</xsl:attribute>
			<xsl:apply-templates select="machineParameters/hostParameter" mode="PARAMETER_MODE"/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="PARAMETER_MODE">
		<xsl:choose>
			<xsl:when test="@name='cpucount'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">cpuCount</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">null</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='cpuspeed'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">cpuSpeed</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">MHz</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='memory'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">memoryTotal</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">MB</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='diskspace'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">diskTotal</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">MB</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='cpuarch'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">cpuType</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">STRING</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='osname'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">osName</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">STRING</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='osversion'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">osVersion</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">STRING</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='freememory'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">memoryFree</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">MB</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='freediskspace'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">diskFree</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">MB</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='bandwidth'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">bandwidth</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">MB/sec</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='latency'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">metric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">latency</xsl:with-param>
					<xsl:with-param name="METRIC_UNIT">sec</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">NUMERIC</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>		
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="PARAMETER_TEMPLATE">
		<xsl:param name="ELEMENT"/>
		<xsl:param name="METRIC_TYPE" select="FALSE"/>
		<xsl:param name="METRIC_UNIT" select="FALSE"/>
		<xsl:param name="VALUE_TYPE"/>

		<xsl:element name="{$ELEMENT}">
			<xsl:if test="$METRIC_UNIT">
				<xsl:attribute name="unit">
					<xsl:value-of select="$METRIC_UNIT"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$METRIC_TYPE">
				<xsl:attribute name="type" namespace="http://www.w3.org/2001/XMLSchema-instance">
					<xsl:value-of select="$METRIC_TYPE"/>
				</xsl:attribute>
			</xsl:if>
		
			<xsl:apply-templates select="paramValue[1]" mode="PARAMETER_VALUE_MODE">
				<xsl:with-param name="VALUE_TYPE">
					<xsl:value-of select="$VALUE_TYPE"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
		
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="paramValue" mode="PARAMETER_VALUE_MODE">
		<xsl:param name="VALUE_TYPE"/>
		
		<xsl:choose>
			<xsl:when test="$VALUE_TYPE='NUMERIC'">
				<xsl:element name="rangeValue">
					<xsl:element name="value">
						<xsl:value-of select="."/>
					</xsl:element>
					<xsl:element name="min">
						<xsl:value-of select="."/>
					</xsl:element>
					<xsl:element name="max">
						<xsl:value-of select="."/>
					</xsl:element>
				</xsl:element>
			</xsl:when>
			<xsl:when test="$VALUE_TYPE='STRING'">
				<xsl:element name="value">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="computingResource" mode="RESOURCE_MODE">
		<xsl:variable name="ID" select="additionalProperties/property[@name=$RES_IDENTIFIER]"/>
		
		<xsl:element name="compResource">
			<xsl:attribute name="id">
				<xsl:value-of select="$ID"/>
			</xsl:attribute>
			<xsl:attribute name="templateIdRef">
				<xsl:value-of select="$ID"/>
			</xsl:attribute>
			<xsl:attribute name="isGateway">
				<xsl:value-of select="additionalProperties/property[@name=$GATEWAY]"/>
			</xsl:attribute>
			<xsl:apply-templates select="machineParameters/hostParameter[@name='hostname']" mode="SPECIFIC_PARAMETER_MODE">
				<xsl:with-param name="METRIC_TYPE">hostName</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="SPECIFIC_PARAMETER_MODE">
		<xsl:param name="METRIC_TYPE"/>
		
			<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="ELEMENT">resourceSpecificMetric</xsl:with-param>
					<xsl:with-param name="METRIC_TYPE">
						<xsl:value-of select="$METRIC_TYPE"/>
					</xsl:with-param>
					<xsl:with-param name="VALUE_TYPE">STRING</xsl:with-param>
			</xsl:call-template>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="resourcesGroup" mode="TEMPLATE_MODE">
		<xsl:element name="netResourceTemplate">
			<xsl:attribute name="id">
				<xsl:value-of select="@groupId"/>
			</xsl:attribute>
			<xsl:apply-templates select="networkParamsInsideGroup/*" mode="PARAMETER_MODE"/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="resourcesGroup" mode="RESOURCE_MODE">
		<xsl:element name="netResource">
			<xsl:attribute name="id">
				<xsl:value-of select="@groupId"/>
			</xsl:attribute>
			<xsl:attribute name="templateIdRef">
				<xsl:value-of select="@groupId"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="resourcesGroup" mode="GROUP_MODE">
		<xsl:element name="resourcesCommunicationGroup">
			<xsl:attribute name="providerAdIdRef">
				<xsl:value-of select="additionalProperties/property[@name=$AD_IDENTIFIER]"/>
			</xsl:attribute>		
			<xsl:attribute name="netResourceIdRef">
				<xsl:value-of select="@groupId"/>
			</xsl:attribute>
			<xsl:attribute name="groupId">
				<xsl:value-of select="additionalProperties/property[@name=$GROUP_IDENTIFIER]"/>
			</xsl:attribute>
			<xsl:attribute name="isGateway">
				<xsl:value-of select="additionalProperties/property[@name=$GATEWAY]"/>
			</xsl:attribute>
			<xsl:for-each select="resources/computingResourceIdRef">
				<xsl:element name="resourceIdRef">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="computingResource" mode="AVAILABILITY_MODE">
		<xsl:if test="availability or machineParameters/../availability">
			<xsl:element name="resourceAvailability">
				<xsl:attribute name="resourceIdRef">
					<xsl:value-of select="additionalProperties/property[@name=$RES_IDENTIFIER]"/>
				</xsl:attribute>
				
				<xsl:apply-templates select="availability/timeSlot" mode="RESOURCE_AVAILABILITY_MODE"/>
				
				<xsl:apply-templates select="machineParameters/hostParameter/availability/timeSlot" mode="PARAMETER_AVAILABILITY_MODE"/>
				
			</xsl:element>
		</xsl:if>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="timeSlot" mode="RESOURCE_AVAILABILITY_MODE">
		<xsl:element name="resourceState">
			<xsl:element name="resourceState">
				<xsl:value-of select="@state"/>
			</xsl:element>
			<xsl:element name="timeInterval">
				<xsl:apply-templates select="."/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="timeSlot" mode="PARAMETER_AVAILABILITY_MODE">
		<xsl:element name="resourceState">
			<xsl:element name="timeInterval">
				<xsl:apply-templates select="."/>
			</xsl:element>
			<xsl:element name="metricAvailability">
				<xsl:apply-templates select="../.." mode="PARAMETER_MODE"/>
				<xsl:element name="state">
					<xsl:value-of select="@state"/>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template name="GRID_LEVEL_TEMPLATE">
		<xsl:variable name="GROUP2GROUP" select="topology/resourcesGroup/networkParamsToGroup"/>
		<xsl:if test="$GROUP2GROUP">
			<xsl:element name="intraADInfo">
				<xsl:attribute name="adId"><xsl:value-of select="$GRID_DOMAIN_IDENTIFIER"/></xsl:attribute>
				<xsl:apply-templates select="$GROUP2GROUP" mode="GRID_DOMAIN_TEMPLATE_MODE"/>	
				<xsl:apply-templates select="$GROUP2GROUP" mode="GRID_DOMAIN_RESOURCE_MODE"/>	
			</xsl:element>
			
			<xsl:element name="interADInfo">
				<xsl:attribute name="isSymmetric">
					<xsl:value-of select="topology/resourcesGroup/networkParamsToGroup[1]/@isSymmetric"/>
				</xsl:attribute>
				<xsl:apply-templates select="$GROUP2GROUP" mode="GRID_LINKS_MODE"/>
			</xsl:element>		
		</xsl:if>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="networkParamsToGroup" mode="GRID_DOMAIN_TEMPLATE_MODE">
		<xsl:element name="netResourceTemplate">
			<xsl:attribute name="id">
				<xsl:value-of select="../@groupId"/>-<xsl:value-of select="@groupId"/>-<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			<xsl:apply-templates  mode="PARAMETER_MODE"/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="networkParamsToGroup" mode="GRID_DOMAIN_RESOURCE_MODE">
		<xsl:element name="netResource">
			<xsl:attribute name="id">
				<xsl:value-of select="../@groupId"/>-<xsl:value-of select="@groupId"/>-<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name="templateIdRef">
				<xsl:value-of select="../@groupId"/>-<xsl:value-of select="@groupId"/>-<xsl:value-of select="generate-id()"/>
			</xsl:attribute>			
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="networkParamsToGroup" mode="GRID_LINKS_MODE">
		<xsl:element name="link">
			<xsl:attribute name="adIdRef">
				<xsl:value-of select="$GRID_DOMAIN_IDENTIFIER"/>
			</xsl:attribute>
			<xsl:attribute name="fromAdId">
				<xsl:value-of select="../additionalProperties/property[@name=$AD_IDENTIFIER]"/>
			</xsl:attribute>			
			<xsl:attribute name="fromGroupId">
				<xsl:value-of select="../additionalProperties/property[@name=$GROUP_IDENTIFIER]"/>
			</xsl:attribute>			
			<xsl:attribute name="toAdId">
				<xsl:value-of select="additionalProperties/property[@name=$AD_IDENTIFIER]"/>
			</xsl:attribute>			
			<xsl:attribute name="toGroupId">
				<xsl:value-of select="additionalProperties/property[@name=$GROUP_IDENTIFIER]"/>
			</xsl:attribute>			
			<xsl:attribute name="netResourceIdRef">
				<xsl:value-of select="../@groupId"/>-<xsl:value-of select="@groupId"/>-<xsl:value-of select="generate-id()"/>
			</xsl:attribute>			
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	

<!-- =====================================================  ====================================================== -->	
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="*">
		!!! missing support for <xsl:value-of select="name()"/> !!!
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
</xsl:transform>