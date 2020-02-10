<?xml version="1.0" encoding="UTF-8"?>

<!-- 
LIMITATIONS:
- zaklada sie ze nie mozna definiowac licznosci grupy procesow dla konkretnych wymagan zasobowych. brak odpowiednika w RTG
-->

<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							 xmlns:xalan="http://xml.apache.org/xalan"
							 extension-element-prefixes="xalan">
                             
	<xsl:strip-space elements="*"/>
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
		
	<xsl:variable name="GRID_AD">Grid</xsl:variable>
	<xsl:variable name="EMPTY_TEMPLATE_ID">EMPTY</xsl:variable>

<!-- ========================   ========================= -->
<!-- =====================================================  ====================================================== -->
	<xsl:template match="/">
		<xsl:element name="rtg">
			<xsl:call-template name="CONTEXT_TEMPLATE"/>
			<xsl:call-template name="INTRA_AD_INFO_TEMPLATE"/>
			<xsl:call-template name="TASK_MAPPINGS_TEMPLATE"/> 
		</xsl:element>	 
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
<!-- =====================================================  ====================================================== -->
	<xsl:template name="CONTEXT_TEMPLATE">
		<xsl:element name="context">
			<xsl:attribute name="rtgType">ResourceRequirementsRtg</xsl:attribute>
			<xsl:attribute name="isComplete">true</xsl:attribute>

			<xsl:element name="providerAdId">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:element>
		</xsl:element>			
	</xsl:template>
<!-- =====================================================  ====================================================== -->
<!-- =====================================================  ====================================================== -->
	<xsl:template name="INTRA_AD_INFO_TEMPLATE">
		<xsl:element name="intraADInfo">
			<xsl:attribute name="adId">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:attribute>
			
			<xsl:variable name="COMP_RES_VARIABLE" select="//resourceRequirements/computingResource"/>
			<xsl:variable name="NET_RES_VARIABLE" select="//processesConnection/networkResource[1] | //groupConnection/networkResource[1]"/>
			
			<!-- needed for resources having no others requirements then hostname -->
			<xsl:call-template name="EMPTY_RES_TEMPLATE"/>
			
			<xsl:apply-templates select="$COMP_RES_VARIABLE" mode="TEMPLATE_MODE">
				<xsl:with-param name="TEMPLATE_TYPE">compResourceTemplate</xsl:with-param>
			</xsl:apply-templates>
			
			<xsl:apply-templates select="$NET_RES_VARIABLE" mode="TEMPLATE_MODE">
				<xsl:with-param name="TEMPLATE_TYPE">netResourceTemplate</xsl:with-param>
			</xsl:apply-templates>

			<xsl:apply-templates select="/resourceRequirements/task" mode="RESOURCE_MODE"/>
			
			<xsl:apply-templates select="$NET_RES_VARIABLE" mode="RESOURCE_MODE"/>
						
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="EMPTY_RES_TEMPLATE">
		<xsl:element name="compResourceTemplate">
			<xsl:attribute name="id">
				<xsl:value-of select="$EMPTY_TEMPLATE_ID"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="TEMPLATE_MODE">
		<xsl:param name="TEMPLATE_TYPE"/>
		<xsl:element name="{$TEMPLATE_TYPE}">
			<xsl:attribute name="id">
				<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			<xsl:choose>
				<xsl:when test="name()='computingResource'">
					<xsl:apply-templates select="hostParameter" mode="PARAMETER_MODE"/>
				</xsl:when>
				<xsl:when test="name()='networkResource'">
					<xsl:apply-templates select="networkParameter" mode="PARAMETER_MODE"/>
				</xsl:when>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="PARAMETER_MODE">
		<xsl:choose>
			<xsl:when test="@name='cpucount'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">cpuCount</xsl:with-param>
					<xsl:with-param name="UNIT">null</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='cpuspeed'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">cpuSpeed</xsl:with-param>
					<xsl:with-param name="UNIT">MHz</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='memory'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">memoryTotal</xsl:with-param>
					<xsl:with-param name="UNIT">MB</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='diskspace'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">diskTotal</xsl:with-param>
					<xsl:with-param name="UNIT">MB</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='cpuarch'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">cpuType</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='osname'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">osName</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='osversion'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">osVersion</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='freememory'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">memoryFree</xsl:with-param>
					<xsl:with-param name="UNIT">MB</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='freediskspace'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">diskFree</xsl:with-param>
					<xsl:with-param name="UNIT">MB</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='bandwidth'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">bandwidth</xsl:with-param>
					<xsl:with-param name="UNIT">MB/sec</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="@name='latency'">
				<xsl:call-template name="PARAMETER_TEMPLATE">
					<xsl:with-param name="TYPE">latency</xsl:with-param>
					<xsl:with-param name="UNIT">sec</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="PARAMETER_TEMPLATE">
		<xsl:param name="TYPE"/>
		<xsl:param name="UNIT" select="FALSE"/>
		
		<xsl:element name="metric">
			<xsl:if test="$UNIT">
				<xsl:attribute name="unit">
					<xsl:value-of select="$UNIT"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:attribute name="type" namespace="http://www.w3.org/2001/XMLSchema-instance">
				<xsl:value-of select="$TYPE"/>
			</xsl:attribute>
			<xsl:apply-templates select="*" mode="PARAMETER_VALUE_MODE"/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="stringValue" mode="PARAMETER_VALUE_MODE">
		<xsl:element name="value">
			<xsl:value-of select="@value"/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="value" mode="PARAMETER_VALUE_MODE">
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
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="range" mode="PARAMETER_VALUE_MODE">
		<xsl:element name="rangeValue">
			<xsl:element name="min">
				<xsl:value-of select="min"/>
			</xsl:element>
			<xsl:element name="max">
				<xsl:value-of select="max"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="min" mode="PARAMETER_VALUE_MODE">
		<xsl:element name="rangeValue">
			<xsl:element name="min">
				<xsl:value-of select="."/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="max" mode="PARAMETER_VALUE_MODE">
		<xsl:element name="rangeValue">
			<xsl:element name="max">
				<xsl:value-of select="."/>
			</xsl:element>
		</xsl:element>
	</xsl:template>		
<!-- =====================================================  ====================================================== -->
<!-- =====================================================  ====================================================== -->
	<xsl:template match="task" mode="RESOURCE_MODE">
		<xsl:choose>
			<xsl:when test="candidateHosts">
				<xsl:variable name="RESOURCES" select=".//resourceRequirements/computingResource"/>
				
				<xsl:choose>
					<xsl:when test="$RESOURCES">
						<xsl:apply-templates select="$RESOURCES" mode="HOSTS_RESOURCE_MODE">
							<xsl:with-param name="CANDIDATE_HOSTS" select="candidateHosts/hostName"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="candidateHosts/hostName" mode="HOSTS_RESOURCE_MODE"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select=".//resourceRequirements/computingResource | .//processes/resourceRequirements/computingResource" mode="RESOURCE_MODE"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="computingResource" mode="HOSTS_RESOURCE_MODE">
		<xsl:param name="CANDIDATE_HOSTS"/>
		<xsl:variable name="RESOURCE" select="."/>
		
		<xsl:for-each select="$CANDIDATE_HOSTS">
			<xsl:apply-templates select="$RESOURCE" mode="RESOURCE_MODE">
				<xsl:with-param name="HOST" select="."/>
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="hostName" mode="HOSTS_RESOURCE_MODE">
		<xsl:element name="compResource">
			<xsl:attribute name="id">
				<xsl:value-of select="$EMPTY_TEMPLATE_ID"/>
				<xsl:value-of select="'-'"/>
				<xsl:value-of select="."/>
			</xsl:attribute>
			<xsl:attribute name="templateIdRef">
				<xsl:value-of select="$EMPTY_TEMPLATE_ID"/>
			</xsl:attribute>
			<xsl:attribute name="isGateway">false</xsl:attribute>
			<xsl:apply-templates select="." mode="RESOURCE_METRIC_MODE"/>
		</xsl:element>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="computingResource" mode="RESOURCE_MODE">
		<xsl:param name="HOST" select="FALSE"/>
		<xsl:element name="compResource">
			<xsl:attribute name="id">
				<xsl:value-of select="generate-id()"/>
				<xsl:if test="$HOST">
					<xsl:value-of select="'-'"/>
					<xsl:value-of select="$HOST"/>
				</xsl:if>
			</xsl:attribute>
			<xsl:attribute name="templateIdRef">
				<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name="isGateway">false</xsl:attribute>
			
			<xsl:apply-templates select="$HOST" mode="RESOURCE_METRIC_MODE"/>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="hostName" mode="RESOURCE_METRIC_MODE">
		<xsl:element name="resourceSpecificMetric">
			<xsl:attribute name="type" namespace="http://www.w3.org/2001/XMLSchema-instance">hostName</xsl:attribute>
			<xsl:element name="value">
				<xsl:value-of select="."/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="networkResource" mode="RESOURCE_MODE">
		<xsl:element name="netResource">
			<xsl:attribute name="id">
				<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name="templateIdRef">
				<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="TASK_MAPPINGS_TEMPLATE">
		<xsl:apply-templates select="/resourceRequirements/task" mode="MAPPINGS_MODE"/>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="task" mode="MAPPINGS_MODE">
		
			<xsl:choose>
				<xsl:when test="requirements/topology/processes">
					<xsl:apply-templates select="requirements/topology">
						<xsl:with-param name="JOB_ID">
							<xsl:value-of select="@jobId"/>
						</xsl:with-param>
						<xsl:with-param name="TASK_ID">
							<xsl:value-of select="@taskId"/>
						</xsl:with-param>
						<xsl:with-param name="USER_DN">
							<xsl:value-of select="@userDN"/>
						</xsl:with-param>
						<xsl:with-param name="SUBMISSION_TIME">
							<xsl:value-of select="@submissionTime"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="MAPPED_TO_TEMPLATE">
						<xsl:with-param name="REQUIREMENTS" select="requirements/resourceRequirements/computingResource"/>
						<xsl:with-param name="HOSTS" select="candidateHosts/hostName"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>						
		
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template name="MAPPED_TO_TEMPLATE">
		<xsl:param name="REQUIREMENTS" select="FALSE"/>
		<xsl:param name="HOSTS" select="FALSE"/>
		
		<xsl:element name="taskMapping">
			<xsl:attribute name="consumerAdId">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:attribute>
			<xsl:attribute name="jobId">
				<xsl:value-of select="@jobId"/>
			</xsl:attribute>
			<xsl:attribute name="taskId">
				<xsl:value-of select="@taskId"/>
			</xsl:attribute>
			<xsl:attribute name="userId">
				<xsl:value-of select="@userDN"/>
			</xsl:attribute>
			<xsl:attribute name="submissionTime">
				<xsl:value-of select="@submissionTime"/>
			</xsl:attribute>
			<xsl:attribute name="allocationStatus">Requested</xsl:attribute>
			
			<xsl:apply-templates select="executionTime"/>
			
			<xsl:choose>
				<xsl:when test="$REQUIREMENTS and $HOSTS">
					<xsl:for-each select="$HOSTS">
						<xsl:apply-templates select="$REQUIREMENTS" mode="MAPPED_TO_MODE">
							<xsl:with-param name="HOST" select="."/>
						</xsl:apply-templates>
					</xsl:for-each>
				</xsl:when>
				<xsl:when test="$REQUIREMENTS and not($HOSTS)">
					<xsl:apply-templates select="$REQUIREMENTS" mode="MAPPED_TO_MODE"/>
				</xsl:when>
				<xsl:when test="not($REQUIREMENTS) and $HOSTS">
					<xsl:apply-templates select="$HOSTS" mode="MAPPED_TO_MODE"/>
				</xsl:when>
				<xsl:when test="not($REQUIREMENTS) and not($HOSTS)">
					<xsl:call-template name="EMPTY_MAPPED_TO_TEMPLATE"/>
				</xsl:when>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="EMPTY_MAPPED_TO_TEMPLATE">
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="computingResource" mode="MAPPED_TO_MODE">
		<xsl:param name="HOST" select="FALSE"/>

		<xsl:element name="mappedTo">
			<xsl:attribute name="providerAdIdRef">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:attribute>
			<xsl:attribute name="providerResourceIdRef">
				<xsl:value-of select="generate-id()"/>
				<xsl:if test="$HOST">
					<xsl:value-of select="'-'"/>
					<xsl:value-of select="$HOST"/>
				</xsl:if>
			</xsl:attribute>
			<xsl:attribute name="isCandidateHost">
				<xsl:choose>
					<xsl:when test="$HOST">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="isRequirement">true</xsl:attribute>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="hostName" mode="MAPPED_TO_MODE">

		<xsl:element name="mappedTo">
			<xsl:attribute name="providerAdIdRef">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:attribute>
			<xsl:attribute name="providerResourceIdRef">
				<xsl:value-of select="$EMPTY_TEMPLATE_ID"/>
				<xsl:value-of select="'-'"/>
				<xsl:value-of select="."/>
			</xsl:attribute>
			<xsl:attribute name="isCandidateHost">true</xsl:attribute>
			<xsl:attribute name="isRequirement">true</xsl:attribute>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	
	
	
	
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="topology">
		<xsl:param name="JOB_ID"/>
		<xsl:param name="TASK_ID"/>
		<xsl:param name="USER_DN"/>
		<xsl:param name="SUBMISSION_TIME"/>

		<xsl:element name="taskMapping">
			<xsl:attribute name="consumerAdId">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:attribute>
			<xsl:attribute name="jobId">
				<xsl:value-of select="$JOB_ID"/>
			</xsl:attribute>
			<xsl:attribute name="taskId">
				<xsl:value-of select="$TASK_ID"/>
			</xsl:attribute>
			<xsl:attribute name="userId">
				<xsl:value-of select="$USER_DN"/>
			</xsl:attribute>
			<xsl:attribute name="submissionTime">
				<xsl:value-of select="$SUBMISSION_TIME"/>
			</xsl:attribute>
			<xsl:attribute name="allocationStatus">Requested</xsl:attribute>
			<xsl:attribute name="priority">
				<xsl:value-of select="count(preceding-sibling::topology)"/>
			</xsl:attribute>
		
			<xsl:apply-templates select="../../executionTime"/>
			
			<xsl:variable name="PROCESSES_VARIABLE" select="processes"/>
			<xsl:variable name="GROUPS_VARIABLE" select="group"/>
					
			<xsl:apply-templates select="$PROCESSES_VARIABLE" mode="PROCESSES_GROUP_MODE"/>
			<xsl:apply-templates select="$PROCESSES_VARIABLE" mode="PROCESSES_GROUP_HOSTS_MAPPING_MODE">
				<xsl:with-param name="HOSTS" select="../../candidateHosts/hostName"/>
			</xsl:apply-templates>
			
			<xsl:apply-templates select="$GROUPS_VARIABLE">
				<xsl:with-param name="PROCESSES_VARIABLE" select="$PROCESSES_VARIABLE"/>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="executionTime">
		<xsl:element name="timeInterval">
			<xsl:if test="timePeriod/periodStart">
				<xsl:element name="lowerBoundedRange">
					<xsl:value-of select="timePeriod/periodStart"/>
				</xsl:element>
			</xsl:if>
			
			<xsl:if test="timePeriod/periodEnd">
				<xsl:element name="upperBoundedRange">
					<xsl:value-of select="timePeriod/periodEnd"/>
				</xsl:element>
			</xsl:if>
			
			<xsl:element name="duration">
				<xsl:value-of select="executionDuration"/>
			</xsl:element>

		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="processes" mode="PROCESSES_GROUP_MODE">			
		<xsl:element name="processesGroup">
		
			<xsl:attribute name="processesGroupId">
				<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			
			<xsl:apply-templates select="processesCount"/>
				
		</xsl:element>

	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="processes" mode="PROCESSES_GROUP_HOSTS_MAPPING_MODE">
		<xsl:param name="HOSTS" select="FALSE"/>
		
		<xsl:variable name="PROCESSES" select="."/>
		<xsl:choose>
			<xsl:when test="$HOSTS">
				<xsl:for-each select="$HOSTS">
					<xsl:apply-templates select="$PROCESSES/resourceRequirements/computingResource" mode="PROCESSES_GROUP_MAPPING_MODE">
						<xsl:with-param name="HOST" select="."/>
					</xsl:apply-templates>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="$PROCESSES/resourceRequirements/computingResource" mode="PROCESSES_GROUP_MAPPING_MODE"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="computingResource" mode="PROCESSES_GROUP_MAPPING_MODE">
		<xsl:param name="HOST" select="FALSE"/>
		
		<xsl:element name="processesGroupMapping">
			<xsl:attribute name="providerAdIdRef">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:attribute>
						
			<xsl:attribute name="providerResourceIdRef">
				<xsl:apply-templates select="." mode="RESOURCE_ID_MODE"/>				
				<xsl:if test="$HOST">
					<xsl:value-of select="'-'"/>
					<xsl:value-of select="$HOST"/>
				</xsl:if>
			</xsl:attribute>	
			<xsl:attribute name="isRequirement">true</xsl:attribute>
			<xsl:attribute name="processesGroupIdRef">
				<xsl:value-of select="generate-id(../..)"/>
			</xsl:attribute>
			<xsl:attribute name="isCandidateHost">
				<xsl:choose>
					<xsl:when test="$HOST">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="RESOURCE_ID_MODE">
		<xsl:value-of select="generate-id()"/>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="processesCount">
		<xsl:element name="processesQty">
			<xsl:choose>
				<xsl:when test="value">
					<xsl:element name="value">
						<xsl:value-of select="value"/>
					</xsl:element>
				</xsl:when>
				<xsl:when test="min">
					<xsl:element name="min">
						<xsl:value-of select="min"/>
					</xsl:element>
				</xsl:when>
				<xsl:when test="max">
					<xsl:element name="max">
						<xsl:value-of select="max"/>
					</xsl:element>
				</xsl:when>
				<xsl:when test="range">
					<xsl:element name="min">
						<xsl:value-of select="range/min"/>
					</xsl:element>
					<xsl:element name="max">
						<xsl:value-of select="range/max"/>
					</xsl:element>
				</xsl:when>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="group">
		<xsl:param name="PROCESSES_VARIABLE"/>
		<xsl:apply-templates select="processesConnection">
			<xsl:with-param name="PROCESSES_VARIABLE" select="$PROCESSES_VARIABLE"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="groupConnection">
			<xsl:with-param name="PROCESSES_VARIABLE" select="$PROCESSES_VARIABLE"/>
		</xsl:apply-templates>		
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="processesConnection">
		<xsl:param name="PROCESSES_VARIABLE"/>
		<xsl:element name="processesCommunicationGroup">
			<xsl:attribute name="providerAdIdRef">
				<xsl:value-of select="$GRID_AD"/>
			</xsl:attribute>
			<xsl:attribute name="netResourceIdRef">
				<xsl:apply-templates select="networkResource[1]" mode="RESOURCE_ID_MODE"/>
			</xsl:attribute>
			<xsl:attribute name="groupId">
				<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			<xsl:variable name="GROUP_ID_VARIABLE" select="../@groupId"/>
			<xsl:for-each select="$PROCESSES_VARIABLE[groupIdReference=$GROUP_ID_VARIABLE]">
				<xsl:element name="processesGroupIdRef">
					<xsl:value-of select="generate-id()"/>
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="groupConnection">
			<xsl:param name="PROCESSES_VARIABLE"/>
		<xsl:variable name="FROM_GROUP_ID_VARIABLE" select="../@groupId"/>
		<xsl:variable name="TO_GROUP_ID_VARIABLE" select="@endpointGroupId"/>
		<xsl:variable name="NET_RESOURCE_ID">
			<xsl:apply-templates select="networkResource[1]" mode="RESOURCE_ID_MODE"/>
		</xsl:variable>
		
		<xsl:for-each select="$PROCESSES_VARIABLE[groupIdReference=$FROM_GROUP_ID_VARIABLE]">
			<xsl:variable name="PROCESS_1" select="."/>
			<xsl:for-each select="$PROCESSES_VARIABLE[groupIdReference=$TO_GROUP_ID_VARIABLE]">
				<xsl:if test="generate-id(.)  != generate-id($PROCESS_1)">
					<xsl:element name="processesCommunicationGroup">
						<xsl:attribute name="providerAdIdRef">
							<xsl:value-of select="$GRID_AD"/>
						</xsl:attribute>
						<xsl:attribute name="netResourceIdRef">
							<xsl:value-of select="$NET_RESOURCE_ID"/>
						</xsl:attribute>
						<xsl:attribute name="groupId">
							<xsl:value-of select="generate-id($PROCESS_1)"/>-<xsl:value-of select="generate-id()"/>
						</xsl:attribute>
						<xsl:element name="processesGroupIdRef">
							<xsl:value-of select="generate-id($PROCESS_1)"/>
						</xsl:element>
						<xsl:element name="processesGroupIdRef">
							<xsl:value-of select="generate-id()"/>
						</xsl:element>
					</xsl:element>	
				</xsl:if>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
</xsl:transform>
