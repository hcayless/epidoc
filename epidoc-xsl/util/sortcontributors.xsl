<?xml version="1.0" encoding="UTF-8"?>
 
 <!-- Start license statement: do not remove 
 
 EpiDoc Standard Stylesheets
 Copyright (C) 2000-2006 by all contributors
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 
 Information about the EpiDoc community can be obtained via
  http://epidoc.sf.net.
 
 End license statement: do not remove -->
 

<xsl:stylesheet xmlns="http://www.tei-c.org/ns/1.0" xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/tei:div">
        <xsl:copy>
            <xsl:apply-templates select="tei:title"/>
            <xsl:apply-templates select="tei:author">
                <xsl:sort select="tei:surname"/>
                <xsl:sort select="tei:forename"/>
            </xsl:apply-templates>
            <xsl:for-each select="tei:respStmt">
                <xsl:sort select="tei:name/tei:surname"/>
                <xsl:sort select="tei:name/tei:forename"/>
                <xsl:copy>
                    <xsl:element name="resp">contributor</xsl:element>
                    <xsl:element name="name"><xsl:value-of select="tei:name/tei:persName"/></xsl:element>
                </xsl:copy>
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="tei:title"><xsl:copy-of select="."/></xsl:template>
    <xsl:template match="tei:author">
        <xsl:copy>
            <xsl:value-of select="tei:persName"/>
        </xsl:copy>
        </xsl:template>
</xsl:stylesheet>