<%@ page import="fsi.Bottle" %>



<div class="fieldcontain ${hasErrors(bean: bottleInstance, field: 'volume', 'error')} required">
	<label for="volume">
		<g:message code="bottle.volume.label" default="Volume" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="volume" value="${fieldValue(bean: bottleInstance, field: 'volume')}" required=""/>
</div>

