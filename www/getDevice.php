
<?php 
require "connection.php";
$mysql_qry = "select * from devices";
$result = mysqli_query($conn ,$mysql_qry);
if(mysqli_num_rows($result) > 0) {
    $devices = "Devices: ";
    while($row = $result->fetch_assoc()) {
        $devices .= $row["Name"] . " ";
        
    }
    echo $devices;
}
else {
echo "Devices not successfully received";
}
 
?>