<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.0" 
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:date="http://exslt.org/dates-and-times">
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
	
	<xsl:include href="date.add.template.xsl"/>
	
	<xsl:param name="TASK_ID"/>
	<xsl:param name="EXEC_DURATION_VALUE"/>
	
	<xsl:strip-space elements="*"/>
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
	
	<xsl:template match="/">
		<xsl:apply-templates select="*" mode="COPY_OF_MODE"/>
	</xsl:template>
	
	<xsl:template match="//task[@taskId=$TASK_ID]" mode="COPY_OF_MODE">
		<xsl:element name="task">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates select="requirements" mode="COPY_OF_MODE"/>
			
			<xsl:if test="executionTime">
				<xsl:element name="executionTime">
					
					<xsl:if test="executionTime/executionDuration">
						<xsl:element name="executionDuration">
							<xsl:value-of select="$EXEC_DURATION_VALUE"/>
						</xsl:element>
					</xsl:if>
					
					<xsl:if test="executionTime/timePeriod">
						<xsl:element name="timePeriod">
						<xsl:copy-of select="executionTime/timePeriod/periodStart"/>
							<xsl:element name="periodEnd">
								<xsl:call-template name="date:add">
								   <xsl:with-param name="date-time" select="executionTime/timePeriod/periodEnd" />
								   <xsl:with-param name="duration" select="$EXEC_DURATION_VALUE" />
								</xsl:call-template>
							</xsl:element>
						</xsl:element>
					</xsl:if>
					
				</xsl:element>
			
			</xsl:if>
		
		</xsl:element>
		
	</xsl:template>
	
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

</xsl:transform>