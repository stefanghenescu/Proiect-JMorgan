# Proiect - J. POO Morgan Chase & Co.
### Ghenescu Stefan

## Overview

This project implements an online banking system using OOP principles in Java. The system  
focuses on key banking elements such as users, accounts, cards, plans, commerciant, transactions 
and reports. In this stage, the project extends the initial implementation by adding new
features like split payments, cashback, commission, user plans and more. The design highlights 
modularity and scalability, following design patterns like Factory, Builder, Strategy and Command.

## Package Structure

### 1. `bank` package

Contains essential components of the banking system:

- **`Bank`**: Central class that manages users, accounts, cards, etc.
- **`User`**: Represents the users of the banking system and implements user-related operations  
  (e.g., adding accounts, cards, transactions).
- **`ExchangeRates`**: Provides exchange rate logic for converting between currencies.
- **`SplitPayment`**: Manages split payments between accounts and processes payments when accepted.

#### 1.1 `accounts` package

Contains different types of account classes and a factory to create them:

- **`Account`**: Base class that defines core functionalities.
- **`ClassicAccount`**: Represents a standard bank account without support for interest rate 
  (this will generate an error). This class extends the Account class.
- **`SavingsAccount`**:This class extends the Account class and has logic for interest rate.
- **`BusinessAccount`**: This class extends the Account class but is not implemented in this 
  project because of the lack of time.
- **`AccountFactory`**: Implements the Factory design pattern to create different account types.

#### 1.2 `cards` package

Contains classes for types of cards:

- **`Card`**: Base class representing a classic bank card.
- **`CardOneTimeUse`**: Represents one-time-use cards, extending the `Card` class.

#### 1.3 `commerciants` package

Manages commerciants and their cashback strategies:

- **`Commerciant`**: Represents a commerciant that receives money, has a cashback strategy and 
  returns the cashback.
- **`CashbackStrategy`**: Interface for different cashback calculation strategies, such as:
  - **`SpendingThresholdCashback`**: Provides cashback based on spending thresholds.
  - **`NrOfTransactionsCashback`**: Provides cashback based on the number of transactions.

### 1.4 `plans` package

Handles user plans and their benefits:

- **`PlanStrategy`**: Interface for defining custom plans (e.g., Gold, Silver).
- **`PlanFactory`**: Implements the Factory pattern to create different user plans.
- **`GoldPlan`, `SilverPlan`, `StudentPlan`, `StandardPlan`**: Concrete implementations of user  
  plans with specific benefits like cashback percentages and commission rates.

### 2. `commands` package

Defines commands that can be executed, following the Command pattern:

- **`CashWithdrawalCommand`**: Handles cash withdrawals from an account.
- **`SendMoneyCommand`**: Facilitates transfers between accounts.
- **`SplitPaymentCommand`**: Splits a payment between multiple users.
- **`AcceptSplitPaymentCommand`**: Accepts a pending split payment for a user.
- **`RejectSplitPaymentCommand`**: Rejects a pending split payment for a user.
- **`UpgradePlanCommand`**: Upgrades a user to a new plan.
- **`WithdrawSavingsCommand`**: Handles withdrawals for savings accounts.
- etc.

### 3. `transactions` package

This package handles transactions and how they are built using the Builder pattern.

- **`Transaction`**: Represents a transaction, storing details about it.
- **`TransactionBuilder`**: Implements the Builder pattern to create `Transaction` objects.  
  Properties like transaction amount, involved accounts and currency are set step-by-step.

### 4. `reports` package

Handles the generation of various reports:

- **`ReportFactory`**: Creates different types of reports based on command input.
- **`ReportStrategy`**: Interface for implementing various report generation strategies:
  - **`ClassicReport`**: Generates a standard report.
  - **`SpendingReport`**: Generates a spending report.

### 5. `utils` package

Contains helper classes:

- **`Utils`**: Provides general-purpose helper methods.
- **`JsonOutput`**: Generates JSON output for different commands.


## Design Patterns

The project incorporates various design patterns to ensure modularity, flexibility and scalability:

### 1. **Factory Pattern**
- **Location**: `AccountFactory`, `PlanFactory`.
- **Motivation**: This pattern makes it easy to create different account types or user plans without  
  having to hard-code the exact class names each time. It keeps the code more flexible and  
  simplifies maintenance in the long run, as new account types or plans can be added without  
  changing a lot of things.

### 2. **Builder Pattern**
- **Location**: `TransactionBuilder`.
- **Motivation**: Building complex objects, like transactions, can be messy if you try to put all  
  the details in a constructor, or have multiple setters, or a lot of classes for different types 
  of transactions. The Builder pattern solves this by constructing a transaction step-by-step, 
  letting you set only the fields you need, which makes the code cleaner and easier to 
  understand. For the transactions, depending on the command, I had to set different fields and 
  some of them where optional, so the Builder pattern was a good choice.

### 3. **Command Pattern**
- **Location**: All classes in the `commands` package.
- **Motivation**: The Command pattern lets us package actions as independent commands.          
  This makes it easier to execute these actions without needing to know the  
  details of how they’re handled, offering more flexibility and making the code simpler to  
  update or extend. For example, if we want to add a new command, we can just create a new
  command class and plug it in without changing a lot of things. Every command has an execute 
  method that is called when the command is processed.

### 4. **Strategy Pattern**
- **Location**: `ReportStrategy`, `CashbackStrategy` and `PlanStrategy`.
- **Motivation**: The Strategy pattern allows us to define some algorithms (strategies)  
  and encapsulate them. This way, we can select and change the strategy  
  at runtime, depending on the situation. It's great when we have different possible behaviors  
  (like report generation or cashback calculations) and want the flexibility to switch between  
  them. For instance, with `PlanStrategy`, I had different implementations depending on the plan 
  for cashback percentages or transaction fees.

## Code Flow

Starting from the `Main` class, the program initializes the `Bank` object, then processes 
commands from input. Each command is then created and using the `execute` method, as I use 
command pattern, commands can be performed. The bank object is used to store all the databases 
for users, accounts, cards, exchange rates etc. Every command interacts with the bank object 
which than retrieves the necessary data and interacts with the other classes in the system 
(e.g. `Account`, `Transaction`, `Card`, `Commerciant` etc.). Users have their own plans and 
commerciants have their own cashback strategies. When paying, depending on the command, cashback 
and commission are calculated. Finally, the program generates the output in JSON format.

## Feedback

Developing this project provided valuable experience with OOP principles and design patterns.  
The modular structure makes it easy to extend, and adding new account types, commands,  
or plans is easy with patterns like Factory and Strategy. When I extended the code from the 
first stage, the design patterns helped a lot. They made it faster to expand the code, 
especially because the code was decoupled.
I am sorry in advanced if the are parts of the code that are irrelevant for this stage and 
are not tested. I left them there as I did not know if there are needed for second part of the 
project.

Overall, the project demonstrates how well-applied OOP principles and design patterns can simplify  
complex systems like banking applications.  
