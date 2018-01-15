
<?php 
require "connection.php";
$Room = $_POST["Room"];
$mysql_qry = "select * from Devices where Rooms_idRoom like (select idRoom from Rooms where nameRoom like '$Room')";
$result = mysqli_query($conn ,$mysql_qry);
if(mysqli_num_rows($result) > 0) {
    $devices = "Devices: ";
    while($row = $result->fetch_assoc()) {
        $devices .= $row["name"] . " "; 
    }
    echo $devices;
}
else {
echo "Devices not successfully received";
}
 
?>