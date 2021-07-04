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

function check_pass() {
  if (
    document.getElementById("password").value != "" &&
    document.getElementById("password").value != ""
  ) {
    if (
      document.getElementById("password").value ==
      document.getElementById("confirm_password").value
    ) {
      document.getElementById("submit").disabled = false;
      document.getElementById("message").style.color = "green";
      document.getElementById("message").innerHTML = "Password match";
    } else {
      document.getElementById("submit").disabled = true;
      document.getElementById("message").style.color = "red";
      document.getElementById("message").innerHTML = "Password does not match";
    }
  }
}
