<% 
    if(request.getUserPrincipal() == null)
        response.sendRedirect("public/login.jsf"); 
    else 
        response.sendRedirect("view/home.jsf"); 
%>