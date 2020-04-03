package ao.corona.coronasos.controller

import ao.corona.coronasos.entity.MedicalSupplyPost
import java.util.*

val medicalSupplyPost = MedicalSupplyPost(
        id = UUID.randomUUID().toString(),
        address = "123 Test Blvd",
        city = "Testville",
        country = "Test-slivakia",
        description = "This is a test medical supply post",
        needNotExcess = false,
        state = "TX",
        supplyAmounts = null,
        testInt = 20,
        username = "Tester Testington"
)