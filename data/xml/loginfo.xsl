<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/">
        <html>
            <body>
            <xsl:for-each select="*/property">
                <b><xsl:value-of select="@name"/>: </b><xsl:value-of select="."/><br/>
            </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
