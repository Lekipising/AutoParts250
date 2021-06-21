// Authors: Liplan Lekipising and catherine Muthoni

function showOrders() {
  document.getElementById("myordersac").style.display = "block";
  document.getElementById("acc").style.display = "none";
}

function showAccount() {
  document.getElementById("acc").style.display = "block";
  document.getElementById("myordersac").style.display = "none";
}

function enableBtn() {
  document.getElementById("submit").disabled = false;
}

var myVar;

function myFunction() {
  myVar = setTimeout(showPage, 3000);
}

function showPage() {
  document.getElementById("loader").style.display = "none";
  document.getElementById("myDiv").style.display = "block";
}
