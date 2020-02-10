<?xml version="1.0" encoding="UTF-8"?>

<!--
TO_DO:
-->


<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
			     xmlns:rtg="http://www.QosCosGrid.org/resourceDescription"
			     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                             
	<xsl:strip-space elements="*"/>
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
	
<!-- =====================================================  ====================================================== -->	
	<xsl:variable name="AD_IDENTIFIER">AD_ID</xsl:variable>
	<xsl:variable name="RES_IDENTIFIER">RES_ID</xsl:variable>
	<xsl:variable name="GROUP_IDENTIFIER">GROUP_ID</xsl:variable>
	<xsl:variable name="GATEWAY">GATEWAY</xsl:variable>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="/rtg:rtg"/>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:rtg">
		<xsl:element name="hostParameters">
		
			<xsl:if test="rtg:context/rtg:defaultTimeInterval">
				<xsl:element name="validity">
					<xsl:call-template name="TIME_STATUS_TEMPLATE">
						<xsl:with-param name="TIME_INTERVAL" select="rtg:context/rtg:defaultTimeInterval"/>
					</xsl:call-template>
				</xsl:element>
			</xsl:if>
		
			<xsl:if test="rtg:intraADInfo/rtg:compResource">
				<xsl:element name="resources">
					<xsl:apply-templates select="rtg:intraADInfo/rtg:compResource"/>
				</xsl:element>
			</xsl:if>
			
			<xsl:if test="rtg:intraADInfo/rtg:resourcesCommunicationGroup">
				<xsl:element name="topology">
					<xsl:apply-templates select="rtg:intraADInfo/rtg:resourcesCommunicationGroup"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>	 
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:compResource">
		<xsl:element name="computingResource">
			<xsl:attribute name="resourceId">
				<xsl:call-template name="IDENTIFIER_TEMPLATE">
					<xsl:with-param name="AD_ID" select="../@adId"/>
					<xsl:with-param name="ID" select="@id"/>
				</xsl:call-template>	
			</xsl:attribute>
			
			<xsl:element name="machineParameters">
				<xsl:apply-templates select="rtg:resourceSpecificMetric" mode="METRIC_MODE"/>
				
				<xsl:variable name="TEMPLATE_ID" select="@templateIdRef"/>				
 				<xsl:apply-templates select="../rtg:compResourceTemplate[@id=$TEMPLATE_ID]/rtg:metric" mode="METRIC_MODE">
 					<xsl:with-param name="RES_ID">
 						<xsl:value-of select="@id"/>
 					</xsl:with-param>
 				</xsl:apply-templates>
			</xsl:element>

			<xsl:variable name="RES_ID" select="@id"/>			
			
			<xsl:if test="../rtg:resourceAvailability[@resourceIdRef=$RES_ID]/rtg:resourceState[rtg:resourceState]">
			    <xsl:element name="availability">
				<xsl:apply-templates select="../rtg:resourceAvailability[@resourceIdRef=$RES_ID]/rtg:resourceState[rtg:resourceState]"/>
			    </xsl:element>
			</xsl:if>
			
			<xsl:element name="additionalProperties">
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$AD_IDENTIFIER"/>
					</xsl:attribute>
					<xsl:value-of select="../@adId"/>
				</xsl:element>
				
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$RES_IDENTIFIER"/>
					</xsl:attribute>
					<xsl:value-of select="@id"/>
				</xsl:element>
				
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$GATEWAY"/>
					</xsl:attribute>
					<xsl:value-of select="@isGateway"/>
				</xsl:element>	
			</xsl:element>

		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="METRIC_MODE">
		<xsl:param name="RES_ID"/>

		<xsl:choose>
			<xsl:when test="@xsi:type='cpuCount'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">cpucount</xsl:with-param>
					<xsl:with-param name="RES_ID"><xsl:value-of select="$RES_ID"/></xsl:with-param>
					<xsl:with-param name="METRIC">cpuCount</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@xsi:type='cpuSpeed'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">cpuspeed</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@xsi:type='memoryTotal'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">memory</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@xsi:type='diskTotal'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">diskspace</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@xsi:type='swapTotal'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">otherParameter</xsl:with-param>
					<xsl:with-param name="NAME">swap</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			
			
			<xsl:when test="@xsi:type='cpuType'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">cpuarch</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			
			<xsl:when test="@xsi:type='osName'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">osname</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			
			<xsl:when test="@xsi:type='osVersion'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">osversion</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			
			<xsl:when test="@xsi:type='hostName'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">hostParameter</xsl:with-param>
					<xsl:with-param name="NAME">hostname</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			
			<xsl:when test="@xsi:type='ip'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">otherParameter</xsl:with-param>
					<xsl:with-param name="NAME">ip</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			

			<xsl:when test="@xsi:type='bandwidth'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">networkParameter</xsl:with-param>
					<xsl:with-param name="NAME">bandwidth</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			
			<xsl:when test="@xsi:type='latency'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">networkParameter</xsl:with-param>
					<xsl:with-param name="NAME">latency</xsl:with-param>
				</xsl:call-template>
			</xsl:when>			
						
			<xsl:otherwise>
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">otherParameter</xsl:with-param>
					<xsl:with-param name="NAME"><xsl:value-of select="@xsi:type"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
			
		</xsl:choose>			
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="PARAMETER_TEMPLATE">
		<xsl:param name="TYPE"/>
		<xsl:param name="NAME"/>
		<xsl:param name="RES_ID" select="FALSE"/>
		<xsl:param name="METRIC" select="FALSE"/>
		
		<xsl:element name="{$TYPE}">
			<xsl:attribute name="name">
				<xsl:value-of select="$NAME"/>
			</xsl:attribute>
	
			<xsl:apply-templates mode="PARAMETER_VALUE_MODE"/>
			
			<xsl:if test="$RES_ID">
				<xsl:apply-templates select="../../rtg:resourceAvailability[@resourceIdRef=$RES_ID]/rtg:resourceState/rtg:metricAvailability[rtg:metric/@xsi:type=$METRIC]"/>
			</xsl:if>
						
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:rangeValue" mode="PARAMETER_VALUE_MODE">
		<xsl:element name="paramValue">
			<xsl:value-of select="rtg:value"/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:value" mode="PARAMETER_VALUE_MODE">
		<xsl:element name="paramValue">
			<xsl:value-of select="."/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:resourceState">
		<xsl:call-template name="TIME_STATUS_TEMPLATE">
			<xsl:with-param name="STATUS" select="rtg:resourceState"/>
			<xsl:with-param name="TIME_INTERVAL" select="rtg:timeInterval"/>
		</xsl:call-template>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="TIME_STATUS_TEMPLATE">
		<xsl:param name="STATUS" select="FALSE"/>
		<xsl:param name="TIME_INTERVAL"/>
		
		<xsl:if test="$TIME_INTERVAL">
			<xsl:element name="timeSlot">
				<xsl:if test="$STATUS">
					<xsl:attribute name="state">
						<xsl:value-of select="$STATUS"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:apply-templates select="$TIME_INTERVAL" mode="TIME_INTERVAL_MODE"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="TIME_INTERVAL_MODE">
		<xsl:element name="slotStart">
			<xsl:value-of select="rtg:lowerBoundedRange"/>
		</xsl:element>
		<xsl:choose>
			<xsl:when test="rtg:upperBoundedRange">
				<xsl:element name="slotEnd">
					<xsl:value-of select="rtg:upperBoundedRange"/>
				</xsl:element>
			</xsl:when>
			<xsl:when test="rtg:duration">
				<xsl:element name="slotDuration">
					<xsl:value-of select="rtg:duration"/>
				</xsl:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:metricAvailability">
		<xsl:element name="availability">
			<xsl:call-template name="TIME_STATUS_TEMPLATE">
				<xsl:with-param name="STATUS" select="rtg:state"/>
				<xsl:with-param name="TIME_INTERVAL" select="../rtg:timeInterval"/>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:resourcesCommunicationGroup">
		<xsl:element name="resourcesGroup">
			<xsl:attribute name="groupId">
				<xsl:call-template name="IDENTIFIER_TEMPLATE">
					<xsl:with-param name="AD_ID" select="../@adId"/>
					<xsl:with-param name="ID" select="@groupId"/>
				</xsl:call-template>
			</xsl:attribute>
			
			<xsl:element name="resources">
				<xsl:apply-templates select="rtg:resourceIdRef">
				</xsl:apply-templates>
			</xsl:element>
			
			<xsl:element name="networkParamsInsideGroup">
				<xsl:variable name="NET_ID" select="@netResourceIdRef"/>
				<xsl:apply-templates select="../rtg:netResource[@id=$NET_ID]"/>
			</xsl:element>
			
			<xsl:variable name="AD_ID" select="../@adId"/>
			<xsl:variable name="GROUP_ID" select="@groupId"/>
			<xsl:apply-templates select="/rtg:rtg/rtg:interADInfo/rtg:link[@fromAdId=$AD_ID and @fromGroupId=$GROUP_ID]"/>
			
			<xsl:element name="additionalProperties">
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$AD_IDENTIFIER"/>
					</xsl:attribute>
					<xsl:value-of select="$AD_ID"/>
				</xsl:element>
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$GROUP_IDENTIFIER"/>
					</xsl:attribute>
					<xsl:value-of select="$GROUP_ID"/>
				</xsl:element>
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$GATEWAY"/>
					</xsl:attribute>
					<xsl:value-of select="@isGateway"/>
				</xsl:element>


			</xsl:element>
			
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:resourceIdRef">
		
		<xsl:element name="computingResourceIdRef">
			<xsl:value-of select="."/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:netResource">
		<xsl:variable name="TEMPLATE_ID" select="@templateIdRef"/>
		<xsl:apply-templates select="../rtg:netResourceTemplate[@id=$TEMPLATE_ID]/rtg:metric" mode="METRIC_MODE"/>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:link">
		<xsl:element name="networkParamsToGroup">
			<xsl:attribute name="groupId">
				<xsl:call-template name="IDENTIFIER_TEMPLATE">
					<xsl:with-param name="AD_ID" select="@toAdId"/>
					<xsl:with-param name="ID" select="@toGroupId"/>
				</xsl:call-template>
			</xsl:attribute>
			<xsl:attribute name="isSymmetric">
				<xsl:value-of select="../@isSymmetric"/>
			</xsl:attribute>
			
			<xsl:variable name="NET_AD_ID" select="@adIdRef"/>
			<xsl:variable name="NET_ID" select="@netResourceIdRef"/>
			<xsl:apply-templates select="/rtg:rtg/rtg:intraADInfo[@adId=$NET_AD_ID]/rtg:netResource[@id=$NET_ID]"/>
			
			<xsl:element name="additionalProperties">
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$AD_IDENTIFIER"/>
					</xsl:attribute>
					<xsl:value-of select="@toAdId"/>
				</xsl:element>
				<xsl:element name="property">
					<xsl:attribute name="name">
						<xsl:value-of select="$GROUP_IDENTIFIER"/>
					</xsl:attribute>
					<xsl:value-of select="@toGroupId"/>
				</xsl:element>
			</xsl:element>
			
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
<!-- =====================================================  ====================================================== -->
	<xsl:template name="IDENTIFIER_TEMPLATE">
		<xsl:param name="AD_ID"/>
		<xsl:param name="ID"/>
		
		<xsl:value-of select="$AD_ID"/>
		<xsl:value-of select="'-'"/>
		<xsl:value-of select="$ID"/>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*">
		BRAK OBSLUGI DLA = <xsl:value-of select="name()"/>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
</xsl:transform>