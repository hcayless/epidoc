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
 

<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:tei="http://www.tei-c.org/ns/1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:template name="doteiheadermetadata">
      <xsl:if test="/tei:TEI.2">
         <xsl:element name="div">
            <xsl:attribute name="id">htmldocumentmeta</xsl:attribute>
            <xsl:element name="h2">Document metadata</xsl:element>
            <xsl:apply-templates mode="teiheadermetadata" select="/tei:TEI.2/tei:teiHeader/tei:fileDesc/tei:titleStmt" />
            <xsl:apply-templates mode="teiheadermetadata" select="/tei:TEI.2/tei:teiHeader/tei:fileDesc/tei:publicationStmt" />
            <xsl:apply-templates mode="teiheadermetadata" select="/tei:TEI.2/tei:teiHeader/tei:fileDesc/tei:sourceDesc" />
            <xsl:apply-templates mode="teiheadermetadata" select="/tei:TEI.2/tei:teiHeader/tei:profileDesc/tei:creation" />
            <xsl:apply-templates mode="teiheadermetadata" select="/tei:TEI.2/tei:teiHeader/tei:revisionDesc" />
         </xsl:element>
      </xsl:if>
   </xsl:template>
   <xsl:template match="tei:titleStmt" mode="teiheadermetadata">
      <xsl:element name="div">
         <xsl:attribute name="id">htmldocumenttitlestmt</xsl:attribute>
         <xsl:element name="h3">Full document title statement (tei:titleStmt)</xsl:element>
         <xsl:apply-templates select="tei:title" mode="teiheadermetadata" />
         <xsl:if test="count(tei:author | tei:editor | tei:translator | tei:respStmt) &gt; 0">
            <xsl:element name="ul">
               <xsl:apply-templates select="tei:author | tei:editor | tei:translator" mode="teiheadermetadata" />
               <xsl:apply-templates select="tei:respStmt" mode="teiheadermetadata" />
            </xsl:element>
         </xsl:if>
      </xsl:element>
   </xsl:template>
   <xsl:template match="tei:title" mode="teiheadermetadata">
      <xsl:element name="p"> Title: <xsl:element name="span">
            <xsl:attribute name="id">htmldocumenttitle</xsl:attribute>
            <xsl:call-template name="dolangattr" />
            <xsl:apply-templates />
         </xsl:element>
      </xsl:element>
   </xsl:template>
   <xsl:template match="tei:author | tei:editor | tei:translator" mode="teiheadermetadata">
      <xsl:element name="li">
         <xsl:choose>
            <xsl:when test="name(.)='author'">author: </xsl:when>
            <xsl:when test="name(.)='editor'">editor: </xsl:when>
            <xsl:when test="name(.)='translator'">translator: </xsl:when>
         </xsl:choose>
         <xsl:apply-templates />
      </xsl:element>
   </xsl:template>
   <xsl:template match="tei:respStmt" mode="teiheadermetadata">
      <xsl:element name="li">
         <xsl:value-of select="tei:resp" />: <xsl:value-of select="tei:name" />
      </xsl:element>
   </xsl:template>
   <xsl:template match="tei:publicationStmt" mode="teiheadermetadata">
      <xsl:element name="div">
         <xsl:attribute name="id">htmldocumentpublicationstmt</xsl:attribute>
         <xsl:element name="h3">Full document publication statement (tei:publicationStmt)</xsl:element>
         <xsl:apply-templates />
      </xsl:element>
   </xsl:template>
   <xsl:template match="tei:sourceDesc" mode="teiheadermetadata">
      <xsl:element name="div">
         <xsl:attribute name="id">htmldocumentsourcedesc</xsl:attribute>
         <xsl:element name="h3">Full document source description (tei:sourceDesc)</xsl:element>
         <xsl:apply-templates />
      </xsl:element>
   </xsl:template>
   <xsl:template match="tei:creation" mode="teiheadermetadata">
      <xsl:element name="div">
         <xsl:attribute name="id">htmldocumentcreation</xsl:attribute>
         <xsl:element name="h3">Document creation</xsl:element>
         <xsl:element name="p">
            <xsl:apply-templates />
         </xsl:element>
      </xsl:element>
   </xsl:template>
   <xsl:template match="tei:revisionDesc" mode="teiheadermetadata">
      <xsl:element name="div">
         <xsl:attribute name="id">htmldocumentrevisiondescription</xsl:attribute>
         <xsl:element name="h3">Full document revision description (tei:revisionDesc)</xsl:element>
         <xsl:element name="ul">
            <xsl:for-each select="tei:change">
               <xsl:element name="li"><xsl:apply-templates select="tei:date" /> (<xsl:apply-templates select="tei:respStmt/tei:name" />, <xsl:apply-templates select="tei:respStmt/tei:resp" />): <xsl:element name="ul">
                     <xsl:apply-templates select="tei:item" />
                  </xsl:element></xsl:element>
            </xsl:for-each>
         </xsl:element>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>