(function() {

    $(document).on("keydown", ".ui-cell-editor-input input", function(event) {
        if (event.keyCode == 13) {
            $(this).closest("tr").find(".ui-row-editor .ui-icon-check").click();
        }
    });

})();


