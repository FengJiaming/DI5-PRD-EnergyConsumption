<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
			     xmlns:rtg="http://www.QosCosGrid.org/resourceDescription"
			     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                             
	<xsl:strip-space elements="*"/>
	
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
	
	<xsl:variable name="AD_IDENTIFIER">AD_ID</xsl:variable>
	<xsl:variable name="RESOURCE_IDENTIFIER">RES_ID</xsl:variable>
	<xsl:variable name="CONSUMER_AD_IDENTIFIER">CONSUMER_AD_ID</xsl:variable>
	<xsl:variable name="PGM_IDENTIFIER">PGM_ID</xsl:variable>


	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="/rtg:rtg"/>
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:rtg">
		<xsl:element name="schedulingPlan">
			<xsl:apply-templates select="rtg:taskMapping"/>
		</xsl:element>	 
	</xsl:template>	
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:taskMapping">	
		<xsl:apply-templates select="rtg:mappedTo[@isRequirement='false']"/>
		
		<xsl:apply-templates select="rtg:processesGroupMapping[@isRequirement='false']">
			<xsl:with-param name="GROUPS" select="rtg:processesGroup"/>
		</xsl:apply-templates>				
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:mappedTo">
	
		<xsl:variable name="ADID" select="@providerAdIdRef"/>
		<xsl:variable name="RESID" select="@providerResourceIdRef"/>
		<xsl:variable name="RESOURCE" select="/rtg:rtg/rtg:intraADInfo[@adId=$ADID]/rtg:compResource[@id=$RESID]/rtg:resourceSpecificMetric[@xsi:type='hostName']"/>

		<xsl:if test="$RESOURCE">
			<xsl:element name="taskAllocation">
				<xsl:attribute name="jobID">
					<xsl:value-of select="../@jobId"/>
				</xsl:attribute>
				<xsl:attribute name="taskID">
					<xsl:value-of select="../@taskId"/>
				</xsl:attribute>
				
				<xsl:if test="@allocationStatus or ../@allocationStatus">
					<xsl:attribute name="allocationStatus">
						<xsl:call-template name="ALLOCATION_STATUS_MAPPING_TEMPLATE">
							<xsl:with-param name="STATUS">
								<xsl:choose>
									<xsl:when test="@allocationStaus">
										<xsl:value-of select="@allocationStatus"/>
									</xsl:when>
									<xsl:when test="../@allocationStatus">
										<xsl:value-of select="../@allocationStatus"/>
									</xsl:when>
								</xsl:choose>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:attribute>
				</xsl:if>
				
				<xsl:apply-templates select="$RESOURCE"/>
				
				<xsl:call-template name="TIME_INTERVAL_TEMPLATE"/>
				
				<xsl:element name="additionalProperties">
					<xsl:element name="property">
						<xsl:attribute name="name">
							<xsl:value-of select="$AD_IDENTIFIER"/>
						</xsl:attribute>
						<xsl:value-of select="$ADID"/>
					</xsl:element>
					<xsl:element name="property">
						<xsl:attribute name="name">
							<xsl:value-of select="$RESOURCE_IDENTIFIER"/>
						</xsl:attribute>
						<xsl:value-of select="$RESID"/>
					</xsl:element>
					<xsl:element name="property">
						<xsl:attribute name="name">
							<xsl:value-of select="$CONSUMER_AD_IDENTIFIER"/>
						</xsl:attribute>
						<xsl:value-of select="../@consumerAdId"/>
					</xsl:element>

				</xsl:element>
			</xsl:element>
		</xsl:if>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template name="TIME_INTERVAL_TEMPLATE">
			<xsl:choose>
				<xsl:when test="rtg:timeInterval">
					<xsl:apply-templates select="rtg:timeInterval" mode="TIME_INTERVAL_MODE"/>
				</xsl:when>
				<xsl:when test="../rtg:timeInterval">
					<xsl:apply-templates select="../rtg:timeInterval" mode="TIME_INTERVAL_MODE"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="/rtg:rtg/rtg:context/rtg:defaultTimeInterval" mode="TIME_INTERVAL_MODE"/>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>
<!-- =====================================================  ====================================================== -->	
	<xsl:template match="rtg:processesGroupMapping">
		<xsl:param name="GROUPS" select="FALSE"/>
		
		<xsl:variable name="ADID" select="@providerAdIdRef"/>
		<xsl:variable name="RESID" select="@providerResourceIdRef"/>
		<xsl:variable name="RESOURCE" select="/rtg:rtg/rtg:intraADInfo[@adId=$ADID]/rtg:compResource[@id=$RESID]/rtg:resourceSpecificMetric[@xsi:type='hostName']"/>
		
		<xsl:if test="$RESOURCE">
			<xsl:element name="taskAllocation">
				<xsl:attribute name="jobID">
					<xsl:value-of select="../@jobId"/>
				</xsl:attribute>
				<xsl:attribute name="taskID">
					<xsl:value-of select="../@taskId"/>
				</xsl:attribute>
				<xsl:attribute name="processGroupID">
					<xsl:value-of select="@processesGroupIdRef"/>
				</xsl:attribute>
				
				<xsl:if test="@allocationStatus or ../@allocationStatus">
					<xsl:attribute name="allocationStatus">
						<xsl:call-template name="ALLOCATION_STATUS_MAPPING_TEMPLATE">
							<xsl:with-param name="STATUS">
								<xsl:choose>
									<xsl:when test="@allocationStaus">
										<xsl:value-of select="@allocationStatus"/>
									</xsl:when>
									<xsl:when test="../@allocationStatus">
										<xsl:value-of select="../@allocationStatus"/>
									</xsl:when>
								</xsl:choose>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:attribute>
				</xsl:if>
				
				<xsl:attribute name="processQuantity">
					<xsl:choose>
						<xsl:when test="rtg:processesQty">
							<xsl:apply-templates select="rtg:processesQty"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="PROCESSESID" select="@processesGroupIdRef"/>
							<xsl:apply-templates select="$GROUPS[@processesGroupId=$PROCESSESID]/rtg:processesQty"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				
				<xsl:apply-templates select="$RESOURCE"/>
	
				<xsl:call-template name="TIME_INTERVAL_TEMPLATE"/>	
				
				<xsl:element name="additionalProperties">
					<xsl:element name="property">
						<xsl:attribute name="name">
							<xsl:value-of select="$AD_IDENTIFIER"/>
						</xsl:attribute>
						<xsl:value-of select="$ADID"/>
					</xsl:element>
					<xsl:element name="property">
						<xsl:attribute name="name">
							<xsl:value-of select="$RESOURCE_IDENTIFIER"/>
						</xsl:attribute>
						<xsl:value-of select="$RESID"/>
					</xsl:element>
					<xsl:element name="property">
						<xsl:attribute name="name">
							<xsl:value-of select="$CONSUMER_AD_IDENTIFIER"/>
						</xsl:attribute>
						<xsl:value-of select="../@consumerAdId"/>
					</xsl:element>
					<xsl:element name="property">
						<xsl:attribute name="name">
							<xsl:value-of select="$PGM_IDENTIFIER"/>
						</xsl:attribute>
						<xsl:value-of select="@id"/>
					</xsl:element>
				</xsl:element>				
			</xsl:element>
		</xsl:if>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:resourceSpecificMetric">
		<xsl:element name="host">
			<xsl:attribute name="hostname">
				<xsl:value-of select="."/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="rtg:processesQty">
		<xsl:value-of select="round(rtg:value)"/>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template match="*" mode="TIME_INTERVAL_MODE">
		<xsl:element name="timeSlot">
			<xsl:element name="startTime">
				<xsl:value-of select="rtg:lowerBoundedRange"/>
			</xsl:element>
			
			<xsl:if test="rtg:upperBoundedRange">
				<xsl:element name="endTime">
					<xsl:value-of select="rtg:upperBoundedRange"/>
				</xsl:element>
			</xsl:if>

			<xsl:if test="rtg:duration">
				<xsl:element name="duration">
					<xsl:value-of select="rtg:duration"/>
				</xsl:element>
			</xsl:if>
			
		</xsl:element>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
	<xsl:template  name="ALLOCATION_STATUS_MAPPING_TEMPLATE">
		<xsl:param name="STATUS"/>
		
		<xsl:choose>
			<xsl:when test="$STATUS='Requested'">REQUESTED</xsl:when>
			<xsl:when test="$STATUS='Reserved'">RESERVED</xsl:when>
			<xsl:when test="$STATUS='PartiallyReserved'">PARTIALLY_RESERVED</xsl:when>
			<xsl:when test="$STATUS='insufficientResourcesRejection'">INSUFFICIENT_RESOURCES_REJECTION</xsl:when>
			<xsl:when test="$STATUS='insufficientBudgetRejection'">INSUFFICIENT_BUDGET_REJECTION</xsl:when>
			<xsl:when test="$STATUS='Pending'">PENDING</xsl:when>
			<xsl:when test="$STATUS='Allocated'">ALLOCATED</xsl:when>
			<xsl:when test="$STATUS='PartiallyAllocated'">PARTIALLY_ALLOCATED</xsl:when>
			<xsl:when test="$STATUS='NotAllocated'">NOT_ALLOCATED</xsl:when>
		</xsl:choose>
	</xsl:template>
<!-- =====================================================  ====================================================== -->
</xsl:transform>