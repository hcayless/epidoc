<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:tei="http://www.tei-c.org/ns/1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xml="http://www.w3.org/XML/1998/namespace">
   <xsl:template name="propagateattrs">
      <xsl:call-template name="propagateattrsprefix" />
      <xsl:attribute name="id">
         <xsl:call-template name="getid" />
      </xsl:attribute>
      <xsl:apply-templates select="@lang" mode="propagateattrs" />
      <xsl:apply-templates select="@rend" mode="propagateattrs" />
      <xsl:call-template name="propagateattrspostfix" />
   </xsl:template>
   <xsl:template match="@lang" mode="propagateattrs">
      <xsl:attribute name="lang" namespace="http://www.w3.org/XML/1998/namespace">
         <xsl:value-of select="." />
      </xsl:attribute>
      <xsl:attribute name="lang">
         <xsl:value-of select="." />
      </xsl:attribute>
   </xsl:template>
   <xsl:template match="@rend" mode="propagateattrs">
      <xsl:comment>WARNING: rend attributes are currently suppressed in propagateattrs.xsl</xsl:comment>
   </xsl:template>
   <xsl:template name="propagateattrsprefix" />
   <xsl:template name="propagateattrspostfix" />
</xsl:stylesheet>
