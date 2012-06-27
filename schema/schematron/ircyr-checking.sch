<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://www.ascc.net/xml/schematron" xmlns:t="http://www.tei-c.org/ns/1.0">
    <ns uri="http://www.tei-c.org/ns/1.0" prefix="t"/>
    <!--<pattern name="Print both cases">
        <rule context="AAA">
            <assert test="BBB">BBB element is missing.</assert>
            <report test="BBB">BBB element is present.</report>
            <assert test="@name">AAA misses attribute name.</assert>
            <report test="@name">AAA contains attribute name.</report>
        </rule>
    </pattern>-->
    <pattern name="Test gap attributes">
        <rule context="//t:gap">
            <report test="(@extent and @quantity)">conflict: @quantity and @extent both present on <name/></report>
            <report test="(@reason='lost' or @reason='omitted') and not(@extent or @quantity or (@atLeast and @atMost))"><name/> needs one of @extent, @quantity or both @atLeast and @atMost</report>
            <report test="(@reason='lost' or @reason='omitted') and not(@unit)"><name/> lost or omitted needs @unit</report>
        </rule>
    </pattern>
    <pattern name="Test space attributes">
        <rule context="//t:space">
            <report test="(@extent and @quantity)">conflict: @quantity and @extent both present on <name/></report>
            <report test="(@reason='lost' or @reason='omitted') and not(@extent or @quantity or (@atleast and @atMost))"><name/> needs one of @extent, @quantity or both @atLeast and @atMost</report>
            <report test="(@reason='lost' or @reason='omitted') and not(@unit)"><name/> lost or omitted needs @unit</report>
        </rule>
    </pattern>
    <pattern name="Check for gaps in supplied">
        <rule context="//t:div[@type='edition']//t:gap[not(@reason='ellipsis')]">
            <report test="ancestor::t:supplied">Supplied may not contain <name/></report>
        </rule>
    </pattern>
    <pattern name="checking for Leiden sigla">
        <!-- the regexes below will only work if you have Schematron set to XPATH version 2.0 in your local environment -->
        <!-- in Oxygen: Options > Preferences > XML > XML Parser > Schematron -->
        <rule context="//t:div[@type='edition']">
            <report test="descendant::text()[not(ancestor::t:desc or ancestor::t:note)][matches(.,'[\[\]\(\)]')]">Brackets and parentheses in epigraphic text</report>
            <report test="descendant::text()[not(ancestor::t:desc or ancestor::t:note)][matches(.,'&#x0323;|&#xE1C0;')]">Underdots in epigraphic text</report>
            <report test="descendant::text()[not(ancestor::t:desc or ancestor::t:note)][matches(.,'&lt;|&gt;')]">Angle brackets in epigraphic text</report>
        </rule>
    </pattern>
    <pattern name="Check for untagged words">
        <rule context="//t:div[@type='edition']">
            <report test="descendant::text()[not(ancestor::t:w or ancestor::t:name or ancestor::t:placeName or ancestor::t:num
                or ancestor::t:orig or ancestor::t:desc or ancestor::t:note)][not(translate(normalize-space(translate(.,',.;:··','')),' ','')='')]">
                Character content needs to be tagged as word or name or number or undefined etc.
            </report>
        </rule>
    </pattern>
    <pattern name="Check for problems with names and persnames">
        <rule context="//t:div[@type='edition']//t:name">
            <report test="not(ancestor::t:persName or ancestor::t:placeName)"><name/> needs to be inside persName or placeName</report>
        </rule>
        <rule context="//t:div[@type='edition']//t:persName">
            <report test="not(@type=('divine','emperor','royal','attested'))"><name/> @type needs to be one of 'divine','emperor','royal','attested'</report>
        </rule>
    </pattern>
    
    
</schema>
