/*
document.addEventListener("DOMContentLoaded", function() {
  var menuItems = document.querySelectorAll(".navbar-nav li");
console.log("El archivo de script.js se cargó correctamente");
  menuItems.forEach(function(item) {
    item.addEventListener("click", function() {
      menuItems.forEach(function(item) {
      console.log("El archivo de script.js se cargó correctamente 12asdasd");
        item.classList.remove("active");
      });

      item.classList.add("active");
    });
  });
});
*/

///----------------------------Formato numerico al perder el foco------------------------------------
function formatNumberPurchase() {
  var numberInput = document.getElementById("articleValuePurchase");
  var formattedNumber = Number(numberInput.value).toLocaleString('es-CO'); // Cambia 'es-ES' por el código de idioma correspondiente
  numberInput.value = formattedNumber;
}
function formatNumberSale() {
  var numberInput = document.getElementById("articleValueSale");
  var formattedNumber = Number(numberInput.value).toLocaleString('es-CO'); // Cambia 'es-ES' por el código de idioma correspondiente
  numberInput.value = formattedNumber;
}

//------------------------------------------------------------------------------------------------
//------------------------------Mostrar Formulario------------------------------------------------
function mostrarFormulario() {
  var formulario = document.getElementById("formNewItem");
  document.getElementById("showForm").value = "true";
   console.log("El archivo de script.js se cargó correctamente 12asdasd");
  if (formulario.hasAttribute("hidden")) {
    formulario.removeAttribute("hidden");
  } else {
    formulario.setAttribute("hidden", "");
  }
}

function toggleForm() {
    var form = document.getElementById("formNewItem");
    var showForm = '${showForm}'; // Obtén el valor actual de showForm
    if (showForm === 'true') {
        form.classList.add("d-none"); // Oculta el formulario
    } else {
        form.classList.remove("d-none"); // Muestra el formulario
    }
}
    function toggleForms() {
        var form = document.getElementById("formNewItem");

        if (form.classList.contains("d-none")) {

            form.classList.remove("d-none"); // Mostrar formulario

        } else {

            form.classList.add("d-none"); // Ocultar formulario
        }

    }

//---------------------------------------------------------------------------------------------

var clientNameInput = document.getElementById("clientName");
var documentNumberClientInput = document.getElementById("documentNumberClient");
var documentTypeClientSelect= document.getElementById("documentTypeClient")

clientNameInput.addEventListener("input", function() {
  if (clientNameInput.validity.valid) {
    document.querySelector(".valid-feedback").style.display = "block";
    document.querySelector(".invalid-feedback").style.display = "none";
    clientNameInput.classList.remove("is-invalid");
    clientNameInput.classList.add("is-valid");
  } else {
    document.querySelector(".valid-feedback").style.display = "none";
    document.querySelector(".invalid-feedback").style.display = "block";
    clientNameInput.classList.remove("is-valid");
    clientNameInput.classList.add("is-invalid");
  }
});

documentNumberClientInput.addEventListener("input", function() {
  if (documentNumberClientInput.validity.valid) {
    document.querySelector(".valid-feedback").style.display = "block";
    document.querySelector(".invalid-feedback").style.display = "none";
    documentNumberClientInput.classList.remove("is-invalid");
    documentNumberClientInput.classList.add("is-valid");
  } else {
    document.querySelector(".valid-feedback").style.display = "none";
    document.querySelector(".invalid-feedback").style.display = "block";
    documentNumberClientInput.classList.remove("is-valid");
    documentNumberClientInput.classList.add("is-invalid");
  }
});

documentTypeClientSelect.addEventListener("change",function() {
 if(documentTypeClientSelect.validity.valid){
     document.querySelector(".valid-feedback").style.display = "block";
     document.querySelector(".invalid-feedback").style.display = "none";
     documentTypeClientSelect.classList.remove("is-invalid");
     documentTypeClientSelect.classList.add("is-valid");
 }
 else{
  document.querySelector(".valid-feedback").style.display = "none";
  document.querySelector(".invalid-feedback").style.display = "block";
  documentTypeClientSelect.classList.remove("is-valid");
  documentTypeClientSelect.classList.add("is-invalid");
  }
});

function showHideDocumentNumber() {
    var documentType = document.getElementById("documentTypeClient").value;
    var documentNumber = document.getElementById("documentNumberClient");

    if (documentType == "Sin_Identificacion") {
         var currentDate = new Date();
                var currentYear = currentDate.getFullYear().toString().substr(-2);
                var currentMonth = (currentDate.getMonth() + 1).toString().padStart(2, "0");
                documentNumber.value = "Gen" + currentYear + currentMonth + Math.floor(Math.random() * 9000 + 1000).toString();
               // documentNumber.disabled = true;
    } else {
        documentNumber.value = "";
        documentNumber.disabled = false;
    }
}

function checkDocumentNumber() {
    var documentType = document.getElementById("documentTypeClient").value;
    var documentNumber = document.getElementById("documentNumberClient");

    if (documentType == "Sin_Identificacion") {
        if (!documentNumber.value.startsWith("gen")) {
            documentNumber.setCustomValidity("Debe comenzar con 'gen'");
        } else {
            documentNumber.setCustomValidity("");
        }
    }
}