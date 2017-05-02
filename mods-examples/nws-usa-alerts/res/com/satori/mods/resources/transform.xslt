<?xml version="1.0"?>
<stylesheet version="2.0"
    xmlns:atom="http://www.w3.org/2005/Atom"
    xmlns:mz="urn:mz.com"
    xmlns="http://www.w3.org/1999/XSL/Transform">
    
    <output method="text" encoding="UTF-8" indent="yes"/>

    <template match="/">
        <for-each select="atom:feed/atom:entry">
            <apply-templates select="."/><text>&#10;</text>
        </for-each>
    </template>

    <template match="atom:content" priority="1">
        <text>"</text>
        <value-of select="mz:process-text(text())"/>
        <text>"</text>
    </template>

    <template match="atom:link" priority="1">
        <apply-templates select="@href"/>
    </template>

    <template match="*[*|@*]">
        <text>{</text>
        <for-each-group select="./*|@*" group-by="local-name()">
            <value-of select="concat('&quot;',local-name(),'&quot;:')"/>
            <if test="count(current-group()) > 1">[</if>
            <for-each select="current-group()">
                <apply-templates select="."/>
                <if test="position()!=last()">,</if>
            </for-each>
            <if test="count(current-group()) > 1">]</if>
            <if test="position()!=last()">,</if>
        </for-each-group>
        <text>}</text>
    </template>

    <template match="*">
        <text>"</text>
        <value-of select="mz:process-text(text())"/>
        <text>"</text>
    </template>

    <template match="text() | @*">
        <text>"</text>
        <value-of select="mz:process-text(.)"/>
        <text>"</text>
    </template>

    <function name="mz:process-text">
        <param name="str"/>
        <value-of
            select="replace(replace(replace(replace($str, '\\', '\\\\'),'&#10;', '\\n'), '&#13;','\\r'), '&quot;','\\&quot;')"
        />
    </function>

</stylesheet>