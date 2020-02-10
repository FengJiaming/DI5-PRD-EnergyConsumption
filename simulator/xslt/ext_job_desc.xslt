<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:set="http://exslt.org/sets" exclude-result-prefixes="set">
	<xsl:strip-space elements="*"/>
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
	<!-- =====================================================================================================================-->
		<xsl:variable name="GLOBAL_COMP_RES_TEMPLATES" select="/grmsJob/resourceTemplates/computingResourceTemplate"/>
		<xsl:variable name="GLOBAL_NET_RES_TEMPLATES" select="/grmsJob/resourceTemplates/networkResourceTemplate"/>		
	<!-- =====================================================================================================================-->
	<xsl:template match="/">
		<xsl:apply-templates select="*" mode="COPY_OF_MODE"/>
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="tasksDefaults" mode="COPY_OF_MODE">
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="resourceTemplates" mode="COPY_OF_MODE">
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="task" mode="COPY_OF_MODE">
		<xsl:element name="task">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates select="description" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="descriptionVariables" mode="COPY_OF_MODE"/>
			
			<xsl:call-template name="USE_DEFAULT_IF_NEEDED">
				<xsl:with-param name="VALUE" select="proxyInfo"/>
				<xsl:with-param name="DEFAULT" select="../tasksDefaults/proxyInfo"/>
			</xsl:call-template>
			<xsl:call-template name="USE_DEFAULT_IF_NEEDED">
				<xsl:with-param name="VALUE" select="candidatHosts"/>
				<xsl:with-param name="DEFAULT" select="../tasksDefaults/candidateHosts"/>
			</xsl:call-template>
			<xsl:call-template name="USE_DEFAULT_IF_NEEDED">
				<xsl:with-param name="VALUE" select="requirements"/>
				<xsl:with-param name="DEFAULT" select="../tasksDefaults/requirements"/>
			</xsl:call-template>
			
			<xsl:apply-templates select="execution" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="postResumeExecution" mode="COPY_OF_MODE"/>

			<xsl:call-template name="USE_DEFAULT_IF_NEEDED">
				<xsl:with-param name="VALUE" select="executionTime"/>
				<xsl:with-param name="DEFAULT" select="../tasksDefaults/executionTime"/>
			</xsl:call-template>
			
			<xsl:apply-templates select="workflow" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="parameterSweep" mode="COPY_OF_MODE"/>			

			<xsl:call-template name="USE_DEFAULT_IF_NEEDED">
				<xsl:with-param name="VALUE" select="faultTolerance"/>
				<xsl:with-param name="DEFAULT" select="../tasksDefaults/faultTolerance"/>
			</xsl:call-template>
			<xsl:apply-templates select="other" mode="COPY_OF_MODE"/>
		</xsl:element>
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template match="requirements" mode="COPY_OF_MODE">
		<xsl:element name="requirements">
		
			<xsl:if test="resourcesTemplates or .//templateIdReference">
				<xsl:element name="resourceTemplates">
					<xsl:apply-templates select="resourceTemplates/*" mode="COPY_OF_MODE"/>
					
					<xsl:call-template name="ADD_TEMPLATE_IF_NEEDED">
						<xsl:with-param name="REFERENCED" select="set:distinct(.//computingResource/templateIdReference)"/>
						<xsl:with-param name="LOCAL_TEMPLATES" select="resourceTemplates/computingResourceTemplate"/>
						<xsl:with-param name="GLOBAL_TEMPLATES" select="$GLOBAL_COMP_RES_TEMPLATES"/>
					</xsl:call-template>
		
					<xsl:call-template name="ADD_TEMPLATE_IF_NEEDED">
						<xsl:with-param name="REFERENCED" select=".//networkResource/templateIdReference"/>
						<xsl:with-param name="LOCAL_TEMPLATES" select="resourceTemplates/networkResourceTemplate"/>
						<xsl:with-param name="GLOBAL_TEMPLATES" select="$GLOBAL_NET_RES_TEMPLATES"/>
					</xsl:call-template>
				</xsl:element>	
			</xsl:if>
	
			<xsl:apply-templates select="resourceRequirements" mode="COPY_OF_MODE"/>
			<xsl:apply-templates select="topology" mode="COPY_OF_MODE"/>
		</xsl:element>
	</xsl:template>
	<!-- =====================================================================================================================-->
	<xsl:template name="USE_DEFAULT_IF_NEEDED">
		<xsl:param name="VALUE"/>
		<xsl:param name="DEFAULT"/>
		<xsl:choose>
			<xsl:when test="$VALUE">
				<xsl:apply-templates select="$VALUE" mode="COPY_OF_MODE"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="$DEFAULT" mode="COPY_OF_MODE"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =====================================================================================================================-->
		<xsl:template name="ADD_TEMPLATE_IF_NEEDED">
			<xsl:param name="REFERENCED"/>
			<xsl:param name="LOCAL_TEMPLATES"/>
			<xsl:param name="GLOBAL_TEMPLATES"/>
						
			<xsl:for-each select="$REFERENCED">
				<xsl:variable name="CURRENT" select="."/>
				<xsl:if test="not($LOCAL_TEMPLATES[@templateId = $CURRENT])">
					<xsl:apply-templates select="$GLOBAL_TEMPLATES[@templateId = $CURRENT]" mode="COPY_OF_MODE"/>
				</xsl:if>
			</xsl:for-each>

		</xsl:template>
	<!-- =====================================================================================================================-->
	<!-- =====================================================================================================================-->
	<xsl:template match="*" mode="COPY_OF_MODE">
		<xsl:choose>
			<xsl:when test="node()">
				<xsl:element name="{name()}">
					<xsl:copy-of select="@*"/>
					<xsl:apply-templates select="node()" mode="COPY_OF_MODE"/>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="{name()}">
					<xsl:copy-of select="@*"/>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =====================================================================================================================-->
</xsl:transform>
