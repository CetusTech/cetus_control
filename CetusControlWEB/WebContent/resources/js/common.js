function conMayusculas(field) {
	field.value = field.value.toUpperCase()
}

function handleCompleteAdd(xhr, status, args) {
	if (args.validationFailed) {
		// Validar Formulario
	} else {
		if (args.showDialog) {
			// Si es verdadero cerrar dialog
			PF('dialogAddUser').hide();
		}
	}
}

function handleLoginRequestAdd(xhr, status, args) {
	if (args.validationFailed || !args.lSuccessfull) {
		jQuery('#dialogAdd').effect("shake", {
			times : 4
		}, 10);
	} else {
		PF('dialogAddVar').hide();
	}
}

function handleComplete(xhr, status, args) {
	if (args.validationFailed) {
		// Validar Formulario
	} else {
		if (args.showDialog) {
			// Si es verdadero cerrar dialog
			dialogUpdateUser.hide();
		}
	}
}

/** BLOQUEAR BOTON DERECHO */
$(document).ready(function() {
	$(document).bind("contextmenu", function(e) {
		return false;
	});
});

function limpiarInput(e) {
	e.value = '';
}