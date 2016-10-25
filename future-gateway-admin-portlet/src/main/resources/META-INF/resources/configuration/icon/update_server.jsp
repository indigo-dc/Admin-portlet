<%@ include file="/init.jsp" %>

<liferay-ui:icon
    message="updpate-fg-server"
    onClick='<%= renderResponse.getNamespace() + "updateServerURLView()" %>'
    url="javascript:;"
/>

<aui:script>
    function <portlet:namespace />updateServerURLView() {
        Liferay.Util.openWindow(
            {
                dialog: {
                    destroyOnHide: true,
                    destroyOnClose: true,
                    on: {
                        destroy: function() {
                            parent.location.reload();
                        }
                    }
                },
                id: '<portlet:namespace />updateServerURLView',
                title: '<%= UnicodeLanguageUtil.get(request, "updpate-fg-server") %>',
                uri: '<liferay-portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcRenderCommandName" value="/fg/setServer" /><portlet:param name="redirect" value="<%= String.valueOf(renderResponse.createRenderURL()) %>" /></liferay-portlet:renderURL>'
            }
        );
    }
</aui:script>