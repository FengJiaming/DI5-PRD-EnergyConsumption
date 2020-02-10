<xsl:transform version="1.0" 
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>

	<xsl:template match="/">
 		<xsl:value-of select="qcgJob/@appId"/>
	</xsl:template>
		
</xsl:transform>