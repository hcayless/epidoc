<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: htm-teidiv.xsl 1447 2008-08-07 12:57:55Z zau $ -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="div">
    <!-- div[@type = 'edition']" and div[starts-with(@type, 'textpart')] can be found in htm-teidivedition.xsl -->
        <div>
          <xsl:if test="parent::body and @type">
            <xsl:attribute name="id">
              <xsl:value-of select="@type"/>
            </xsl:attribute>
          </xsl:if>
          <!-- Temporary headings so we know what is where -->
          <xsl:if test="not(head)">
            <xsl:choose>
              <xsl:when test="@type = 'translation'">
                <h2>
                  <xsl:value-of select="/TEI.2/teiHeader/profileDesc/langUsage/language[@id = current()/@lang]"/>
                  <xsl:text> </xsl:text>
                  <xsl:value-of select="@type"/>
                </h2>
              </xsl:when>
              <xsl:otherwise>
                <h2>
                  <xsl:value-of select="@type"/>
                  <xsl:if test="string(@subtype)">
                    <xsl:text>: </xsl:text>
                    <xsl:value-of select="@subtype"/>
                  </xsl:if>
                </h2>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>

          <!-- Body of the div -->
          <xsl:apply-templates/>

          
          <!-- HGV translation editors -->
          <xsl:if test="$leiden-style = 'ddbdp' and @type = 'translation'">
            <xsl:choose>
              <xsl:when test="@lang = 'de'">
                <xsl:text>(DK)</xsl:text>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>(JMSC)</xsl:text>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>
        </div>

  </xsl:template>

</xsl:stylesheet>