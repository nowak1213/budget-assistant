# budget-assistant
To build the project go to main project folder ("budget-assistant") and use: "mvn clean install" (Java 11) </br>
To run the project go to main project folder ("budget-assistant") and use: "mvn spring-boot:run" </br>

It’s simple home budgeting assistant.
There are three endpoints to use:
  1. localhost:8080/budget/assistant with GET HttpMethod -> It’ll show you the status of current registers
  2. localhost:8080/budget/assistant/recharge/{amount} with POST HttpMethod -> You can recharge your wallet with given amount
  3. http://localhost:8080/budget/assistant/transfer/1000.0?from=WALLET&to=INSURANCE_POLICY with POST HttpMethod -> You can transfer money from one register to another. </br>
  In that case amount is 1000, money is going from WALLET into INSURANCE_POLICY register. 
  Registers that can be used: WALLET, SAVINGS, INSURANCE_POLICY, FOOD_EXPENSES
