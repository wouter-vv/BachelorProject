<?php 
require "conn.php";
//$user_name = $_POST["user_name"];
//$user_pass = $_POST["password"];
$user_name = "Thomas";
$user_pass = "Azerty123";
$mysql_qry = "select * from users where Name like '$user_name' and Password like '$user_pass';";
$result = mysqli_query($conn ,$mysql_qry);
if(mysqli_num_rows($result) > 0) {
echo "login success !!!!! Welcome user";
}
else {
echo "login not success";
}
 
?>