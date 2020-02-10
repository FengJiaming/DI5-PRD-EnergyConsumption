<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:jsdl="http://schemas.ggf.org/jsdl/2005/11/jsdl" 
	xmlns:jsdl-posix="http://schemas.ggf.org/jsdl/2005/11/jsdl-posix"
	xmlns:msxsl="urn:schemas-microsoft-com:xslt" 
	xmlns:xalan="http://xml.apache.org/xalan"
	extension-element-prefixes="msxsl">

	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

	<xsl:variable name="FILE_URL_PREFIX">file:///</xsl:variable>	
	
	<xsl:key name="FILESYSTEMS" match="/jsdl:JobDefinition/jsdl:JobDescription/jsdl:Resources/jsdl:FileSystem" use="@name"/>
	
	<xsl:template match="/">
			<xsl:if test="(
				not(jsdl:JobDefinition/jsdl:JobDescription/jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Executable/text()) and 
				not(jsdl:JobDefinition/jsdl:JobDescription/jsdl:Application/jsdl:ApplicationName/text())
			) or (
				jsdl:JobDefinition/jsdl:JobDescription/jsdl:Application/jsdl:ApplicationName/text() and 
				jsdl:JobDefinition/jsdl:JobDescription/jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Executable/text()
			)">
			<xsl:message terminate="no">
				Either ApplicationName or Executable can be specified in the same time
			</xsl:message>			
		</xsl:if>
		
		<xsl:apply-templates select="jsdl:JobDefinition/jsdl:JobDescription"/>		
	</xsl:template>
	
	<xsl:template match="jsdl:JobDescription">
		<xsl:element name="qcgJob">

			<xsl:attribute name="appid">
				<xsl:value-of select="jsdl:JobIdentification/jsdl:JobName/text()"/>
			</xsl:attribute>
			
			<xsl:if test="jsdl:JobIdentification/jsdl:JobProject">
				<xsl:attribute name="project">
					<xsl:value-of select="jsdl:JobIdentification/jsdl:JobProject"/>
				</xsl:attribute>
			
				<xsl:if test="count(jsdl:JobIdentification/jsdl:JobProject) &gt; 1">
					<xsl:message terminate="no">
						Job can belong to ONE project at the most
					</xsl:message>
				</xsl:if>	
		</xsl:if>
		
		<xsl:apply-templates select="jsdl:JobIdentifier/jsdl:Description"/>
		
		<xsl:call-template name="TASK_TEMPLATE"/>
			
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl:Description" mode="JOB_MODE">
		<xsl:element name="JobNode">
			<xsl:value-of select="jsdl:Description"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="TASK_TEMPLATE">
		<xsl:element name="task">
		
			<xsl:attribute name="taskid">
				<xsl:choose>
					<xsl:when test="jsdl:JobIdentifier/jsdl:JobIdentification/jsdl:JobName">
						<xsl:value-of select="jsdl:JobIdentifier/jsdl:JobIdentification/jsdl:JobName"/>
					</xsl:when>
					<xsl:otherwise>task</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			
			<xsl:if test="count(jsdl:DataStaging/jsdl:DeleteOnTermination[text() = 'false']) &gt; 0">
					<xsl:attribute name="persistent">true</xsl:attribute>
			</xsl:if>
			
			<xsl:apply-templates select="jsdl:Resources/jsdl:CandidateHosts"/>
						
			<xsl:if test="jsdl:Application/jsdl:ApplicationName or jsdl:Resources">
				<xsl:element name="resource">
					<xsl:apply-templates select="jsdl:Resources" mode="RESOURCES_MODE"/>		
					<xsl:if test="jsdl:Application/ApplicationName">
						<xsl:element name="applications">
							<xsl:apply-templates select="jsdl:ApplicationName">
								<xsl:with-param name="VERSION">
									<xsl:value-of select="jsdl:ApplicationVersion"/>
								</xsl:with-param>
							</xsl:apply-templates>
						</xsl:element>
					</xsl:if>			
				</xsl:element>
			</xsl:if>
			
			<xsl:if test="jsdl:Resources/jsdl:IndividualCPUSpeed or
			                    jsdl:Resources/jsdl:IndividualCPUCount or
			                    jsdl:Resources/jsdl:IndividualNetworkBandwidth or
			                    jsdl:Resources/jsdl:IndividualPhysicalMemory or
			                    jsdl:Resources/jsdl:IndividualDiskSpace">
				<xsl:element name="hardConstraints">
					<xsl:apply-templates select="jsdl:Resources/jsdl:IndividualCPUSpeed"/>
					<xsl:apply-templates select="jsdl:Resources/jsdl:IndividualCPUCount"/>
					<xsl:apply-templates select="jsdl:Resources/jsdl:IndividualNetworkBandwidth"/>
					<xsl:apply-templates select="jsdl:Resources/jsdl:IndividualPhysicalMemory"/>
					<xsl:apply-templates select="jsdl:Resources/jsdl:IndividualDiskSpace"/>
				</xsl:element>
			</xsl:if>

			<xsl:element name="executable">
				<xsl:attribute name="type">single</xsl:attribute>
				
				<xsl:apply-templates select="jsdl:Application/jsdl:ApplicationName">
					<xsl:with-param name="VERSION">
						<xsl:value-of select="jsdl:Application/jsdl:ApplicationVersion"/>
					</xsl:with-param>
				</xsl:apply-templates>
				
				<xsl:apply-templates select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Executable"/>
			
				<xsl:if test="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Argument or jsdl:DataStaging">
					<xsl:element name="arguments">
						<xsl:apply-templates select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Argument"/>
						<xsl:apply-templates select="jsdl:DataStaging"/>
					</xsl:element>
				</xsl:if>	
				
				<xsl:if test="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Input">
					<xsl:element name="stdin">
						<xsl:element name="url">
							<xsl:call-template name="FILESYSTEM_VALUE">
								<xsl:with-param name="FILESYSTEM_NAME">
									<xsl:value-of select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Input/@filesystemName"/>
								</xsl:with-param>
								<xsl:with-param name="VALUE">
									<xsl:value-of select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Input"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:element>	
					</xsl:element>
				</xsl:if>
	
				<xsl:if test="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Output">
					<xsl:element name="stdout">
						<xsl:element name="url">
							<xsl:call-template name="FILESYSTEM_VALUE">
								<xsl:with-param name="FILESYSTEM_NAME">
									<xsl:value-of select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Output/@filesystemName"/>
								</xsl:with-param>
								<xsl:with-param name="VALUE">
									<xsl:value-of select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Output"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:element>	
					</xsl:element>
				</xsl:if>
	
				<xsl:if test="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Error">
					<xsl:element name="stderr">
						<xsl:element name="url">
							<xsl:call-template name="FILESYSTEM_VALUE">
								<xsl:with-param name="FILESYSTEM_NAME">
									<xsl:value-of select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Error/@filesystemName"/>
								</xsl:with-param>
								<xsl:with-param name="VALUE">
									<xsl:value-of select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Error"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:element>	
					</xsl:element>
				</xsl:if>
				
				<xsl:if test="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Environment">
					<xsl:element name="environment">
						<xsl:apply-templates select="jsdl:Application/jsdl-posix:POSIXApplication/jsdl-posix:Environment"/>
					</xsl:element>
				</xsl:if>
			
			</xsl:element>
			
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl:CandidateHosts">
		<xsl:element name="candidateHosts">
			<xsl:for-each select="jsdl:HostName">
				<xsl:element name="hostName">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl:ApplicationName">
		<xsl:param name="VERSION"/>
		<xsl:element name="application">
			<xsl:if test="$VERSION">
				<xsl:attribute name="version">
					<xsl:value-of select="$VERSION"/>
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>

	<xsl:template match="jsdl:Resources" mode="RESOURCES_MODE">
		<xsl:apply-templates select="jsdl:OperatingSystem/jsdl:OperatingSystemType"/>
		<xsl:apply-templates select="jsdl:OperatingSystem/jsdl:OperatingSystemVersion"/>
	</xsl:template>
		
	<xsl:template match="jsdl-posix:Executable">
		<xsl:element name="execfile">
			<xsl:attribute name="name">
				<xsl:call-template name="GET_FILE_NAME">
					<xsl:with-param name="FILE_PATH">
						<xsl:value-of select="."/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:attribute>
			<xsl:element name="url">
				<xsl:call-template name="FILESYSTEM_VALUE">
					<xsl:with-param name="FILESYSTEM_NAME">
						<xsl:value-of select="@filesystemName"/>
					</xsl:with-param>
					<xsl:with-param name="VALUE">
						<xsl:value-of select="."/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl-posix:Argument">
		<xsl:element name="value">
			<xsl:call-template name="FILESYSTEM_VALUE">
				<xsl:with-param name="FILESYSTEM_NAME">
					<xsl:value-of select="@filesystemName"/>
				</xsl:with-param>
				<xsl:with-param name="VALUE">
					<xsl:value-of select="."/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl:DataStaging">
		<xsl:element name="file">
			<xsl:attribute name="name">
				<xsl:call-template name="FILESYSTEM_VALUE">
					<xsl:with-param name="FILESYSTEM_NAME">
						<xsl:value-of select="jsdl:FilesystemName"/>
					</xsl:with-param>
					<xsl:with-param name="VALUE">
						<xsl:value-of select="jsdl:FileName"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:attribute>
			
			<xsl:attribute name="type">
				<xsl:choose>
					<xsl:when test="jsdl:Source/jsdl:URI">in</xsl:when>
					<xsl:when test="jsdl:Target/jsdl:URI">out</xsl:when>
					<xsl:otherwise>
						<xsl:message terminate="no">
							Source or Target URI is missing
						</xsl:message>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			
			<xsl:if test="jsdl:CreationFlag = 'append'">
				<xsl:attribute name="append">true</xsl:attribute>
			</xsl:if>
			
			<xsl:element name="url">
				<xsl:choose>
					<xsl:when test="jsdl:Source/jsdl:URI">
						<xsl:value-of select="jsdl:Source/jsdl:URI"/>
					</xsl:when>
					<xsl:when test="jsdl:Target/jsdl:URI">
						<xsl:value-of select="jsdl:Target/jsdl:URI"/>
					</xsl:when>
				</xsl:choose>
			</xsl:element>
			
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl-posix:Environment">
		<xsl:element name="variable">
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:call-template name="FILESYSTEM_VALUE">
				<xsl:with-param name="FILESYSTEM_NAME">
					<xsl:value-of select="@filesystemName"/>
				</xsl:with-param>
				<xsl:with-param name="VALUE">
					<xsl:value-of select="."/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl:OperatingSystemType">
		<xsl:element name="ostype">
			<xsl:value-of select="jsdl:OperatingSystemName"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="jsdl:OperatingSystemVersion">
		<xsl:element name="osversion">
			<xsl:value-of select="."/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl:IndividualCPUSpeed">
		<xsl:call-template name="HARD_CONSTRAINT_TEMPLATE">
			<xsl:with-param name="NAME">cpuspeed</xsl:with-param>
			<xsl:with-param name="VALUES" select="*"/>
			<xsl:with-param name="FACTOR">0.001</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="jsdl:IndividualCPUCount">
		<xsl:call-template name="HARD_CONSTRAINT_TEMPLATE">
			<xsl:with-param name="NAME">cpucount</xsl:with-param>
			<xsl:with-param name="VALUES" select="*"/>
			<xsl:with-param name="FACTOR">1</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="jsdl:IndividualNetworkBandwidth">
		<xsl:call-template name="HARD_CONSTRAINT_TEMPLATE">
			<xsl:with-param name="NAME">bandwidth</xsl:with-param>
			<xsl:with-param name="VALUES" select="*"/>
			<xsl:with-param name="FACTOR">
				<xsl:value-of select="1 div (8 * 1024)"/>
			</xsl:with-param>
		</xsl:call-template>	
	</xsl:template>
	
	<xsl:template match="jsdl:IndividualPhysicalMemory">
		<xsl:call-template name="HARD_CONSTRAINT_TEMPLATE">
			<xsl:with-param name="NAME">memory</xsl:with-param>
			<xsl:with-param name="VALUES" select="*"/>
			<xsl:with-param name="FACTOR">
				<xsl:value-of select="1 div 1024"/>
			</xsl:with-param>
		</xsl:call-template>	
	</xsl:template>
	
	<xsl:template match="jsdl:IndividualDiskSpace">
		<xsl:call-template name="HARD_CONSTRAINT_TEMPLATE">
			<xsl:with-param name="NAME">diskspace</xsl:with-param>
			<xsl:with-param name="VALUES" select="*"/>
			<xsl:with-param name="FACTOR">1</xsl:with-param>
		</xsl:call-template>	
	</xsl:template>

	
	<xsl:template name="HARD_CONSTRAINT_TEMPLATE">
		<xsl:param name="NAME"/>
		<xsl:param name="VALUES"/>
		<xsl:param name="FACTOR"/>
		<xsl:element name="parameter">
			<xsl:attribute name="name">
				<xsl:value-of select="$NAME"/>
			</xsl:attribute>
<!--		<xsl:apply-templates select="msxsl:node-set($VALUES)"> 
-->
		<xsl:apply-templates select="xalan:nodeset($VALUES)">
				<xsl:with-param name="FACTOR" select="$FACTOR"/>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="jsdl:UpperBoundedRange">
		<xsl:param name="FACTOR"/>
		<xsl:element name="range">
			<xsl:element name="max">
				<xsl:value-of select=". * $FACTOR"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template match="jsdl:LowerBoundedRange">
		<xsl:param name="FACTOR"/>
		<xsl:element name="range">
			<xsl:element name="min">
				<xsl:value-of select=". * $FACTOR"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template match="jsdl:Exact">
		<xsl:param name="FACTOR"/>
		<xsl:element name="value">
			<xsl:value-of select=". * $FACTOR"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="jsdl:Range">
		<xsl:param name="FACTOR"/>
		<xsl:element name="range">
			<xsl:element name="min">
				<xsl:value-of select="jsdl:LowerBound * $FACTOR"/>
			</xsl:element>
			<xsl:element name="max">
				<xsl:value-of select="jsdl:UpperBound * $FACTOR"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>



	
<xsl:template name="FILESYSTEM_VALUE">
	<xsl:param name="FILESYSTEM_NAME"/>
	<xsl:param name="VALUE"/>
	<xsl:choose>
		<xsl:when test="$FILESYSTEM_NAME and $FILESYSTEM_NAME != ''">
			<xsl:variable name="FILESYSTEM"  select="key('FILESYSTEMS', string($FILESYSTEM_NAME))"/>
			<xsl:choose>
				<xsl:when test="$FILESYSTEM">
					<xsl:value-of select="$FILESYSTEM/jsdl:MountPoint"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:message terminate="no">
						Delcaration of following filesystem is missing: <xsl:value-of select="$FILESYSTEM_NAME"/>
					</xsl:message>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="$VALUE"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test="starts-with($VALUE, '/')">
				<xsl:value-of select="$FILE_URL_PREFIX"/>
			</xsl:if>
			<xsl:value-of select="$VALUE"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="GET_FILE_NAME">
	<xsl:param name="FILE_PATH"/>
		
	<xsl:choose>
		<xsl:when test="not(contains($FILE_PATH,'/'))">
			<xsl:value-of select="$FILE_PATH"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="GET_FILE_NAME">
				<xsl:with-param name="FILE_PATH">
					<xsl:value-of select="substring-after($FILE_PATH,'/')"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
		
</xsl:stylesheet>
