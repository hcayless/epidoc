<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id$ -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="text()[not(ancestor::note)]">
    <xsl:choose>
      <xsl:when test="$edition-type = 'diplomatic' and ancestor::div[@type='edition'] and not(ancestor::head)">
        <xsl:value-of select="translate(translate(., '&#xb7;&#x387;&#xa; ,.;', ''), $all-grc, $grc-upper-strip)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="."/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>