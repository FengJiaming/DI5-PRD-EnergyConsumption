<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							    xmlns:xalan="http://xml.apache.org/xalan"
                               			   extension-element-prefixes="xalan">
		<xsl:strip-space elements="*"/>
		<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
		
		<xsl:param name="JOB_ID"/>
		<xsl:param name="USER_DN"/>
		<xsl:param name="SUBMISSION_TIME"/>	
<!--		<xsl:param name="DOMAIN"/> -->
		<!-- =====================================================================================================================-->
		<xsl:variable name="DEFAULT_FAULT_TOLERANCE" select="/task/faultTolerance"/>
		<xsl:variable name="COMP_RES_TEMPLATE" select="/task/requirements/resourceTemplates/computingResourceTemplate"/>
		<xsl:variable name="NET_RES_TEMPLATE" select="/task/requirements/resourceTemplates/networkResourceTemplate"/>
	<!-- =====================================================================================================================-->
	<xsl:template match="/">
		
			<xsl:apply-templates select="task"/>
		  
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="task">
		<xsl:element name="task">
		
			<xsl:attribute name="jobId"><xsl:value-of select="$JOB_ID"/></xsl:attribute>
			<xsl:attribute name="taskId"><xsl:value-of select="@taskId"/></xsl:attribute>
			<xsl:attribute name="userDN"><xsl:value-of select="$USER_DN"/></xsl:attribute>
			<xsl:attribute name="submissionTime"><xsl:value-of select="$SUBMISSION_TIME"/></xsl:attribute>
<!--			<xsl:attribute name="domain"><xsl:value-of select="$DOMAIN"/></xsl:attribute> -->
			<xsl:if test="@extension">
				<xsl:attribute name="extension"><xsl:value-of select="@extension"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="@priority">
				<xsl:attribute name="priority"><xsl:value-of select="@priority"/></xsl:attribute>
			</xsl:if>
			
			<xsl:apply-templates select="description" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="proxyInfo" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="candidateHosts" mode="COPY_OF_MODE"/>
			
			<xsl:apply-templates select="requirements" mode="COPY_OF_MODE"/>
			
			<xsl:apply-templates select="execution" mode="COPY_OF_MODE"/>
						
			<xsl:apply-templates select="executionTime" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="workflow" mode="COPY_OF_MODE"/>
		</xsl:element>
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="requirements" mode="COPY_OF_MODE">
		<xsl:element name="requirements">
			<xsl:apply-templates select="resourceRequirements" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="topology" mode="COPY_OF_MODE"/>
		</xsl:element>
	</xsl:template> 
	<!-- =====================================================================================================================-->
	<xsl:template match="computingResource" mode="COPY_OF_MODE">
		<xsl:element name="computingResource">
			<xsl:attribute name="resourceId"><xsl:value-of select="@resourceId"/></xsl:attribute>
			<xsl:apply-templates mode="COPY_OF_MODE"/>
			<xsl:if test="not(faultTolerance)">
				<xsl:apply-templates select="$DEFAULT_FAULT_TOLERANCE" mode="COPY_OF_MODE"/>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="templateIdReference" mode="COPY_OF_MODE">
		<xsl:variable name="TEMPLATE_ID" select="."/>
		<xsl:choose>
			<xsl:when test="name(..) = 'computingResource'">
				<xsl:apply-templates select="$COMP_RES_TEMPLATE[@templateId = $TEMPLATE_ID]/*" mode="COPY_OF_MODE"/>
			</xsl:when>
			<xsl:when test="name(..) = 'networkResource'">
				<xsl:apply-templates select="$NET_RES_TEMPLATE[@templateId = $TEMPLATE_ID]/*" mode="COPY_OF_MODE"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="execution" mode="COPY_OF_MODE">
			<xsl:element name="execution">
				<xsl:apply-templates select="executable" mode="COPY_OF_MODE"/>
				<xsl:apply-templates select="resourceConsumptionProfile" mode="COPY_OF_MODE"/>
				<xsl:apply-templates select="stdin" mode="COPY_OF_MODE"/>
				<xsl:apply-templates select="stdout" mode="COPY_OF_MODE"/>
			</xsl:element>
	</xsl:template> 
	<!-- =====================================================================================================================-->
	<xsl:template match="stageInOut" mode="COPY_OF_MODE">
		<xsl:element name="stageInOut">
			<xsl:apply-templates select="*" mode="COPY_OF_MODE"/>
		</xsl:element>
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="file | directory" mode="COPY_OF_MODE">
		<xsl:element name="{name()}">
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>			
			<xsl:if test="@size">
				<xsl:attribute name="size">
					<xsl:value-of select="@size"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@required">
				<xsl:attribute name="required">
					<xsl:value-of select="@required"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates select="*" mode="COPY_OF_MODE"/>
		</xsl:element>
	</xsl:template> 
	<!-- =====================================================================================================================-->
	<xsl:template match="*" mode="COPY_OF_MODE">
	       <xsl:choose>
		  <xsl:when test="node()"><xsl:element name="{name()}"><xsl:copy-of select="@*"/><xsl:apply-templates select="node()" mode="COPY_OF_MODE"/></xsl:element></xsl:when>
		  <xsl:otherwise><xsl:element name="{name()}"><xsl:copy-of select="@*"/></xsl:element></xsl:otherwise>
		</xsl:choose>	
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="*">
	!!! BRAK OBSLUGI DLA <xsl:value-of select="name()"/> !!!
	</xsl:template>
	<!-- =====================================================================================================================-->
</xsl:transform>
