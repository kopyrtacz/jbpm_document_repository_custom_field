START<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
<!--<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>-->
<link rel="stylesheet" type="text/css" href="http://localhost:8080/el/css/elfinder.min.css">
<link rel="stylesheet" type="text/css" href="http://localhost:8080/el/css/theme.css">
<script src="http://localhost:8080/el/js/elfinder.min.js"></script>
<script src="http://localhost:8080/el/js/i18n/elfinder.pl.js"></script>
<script type="text/javascript" charset="utf-8">
        $(document).ready(function() {
            $('#${inputId}').elfinder({
                url : 'http://localhost:8080/elfinder-servlet/elfinder-servlet/connector',
                commandsOptions: {
                    getfile: { multiple: true }
                },
                getFileCallback : function(file) {
                    var urls = $.map(file, function(f) { return f.url; });
                    alert(urls);
                },
                 lang: 'pl'
           });
        });
</script>
<div id="${inputId}"></div>
STOP