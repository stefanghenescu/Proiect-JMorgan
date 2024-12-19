# Proiect Etapa 1 - J. POO Morgan Chase & Co.
### Ghenescu Stefan 322CA

## Overview

This project implements a simplified online banking system using OOP principles in Java. The system 
focuses on key banking features such as managing users, accounts, cards, transactions and reports.
The design highlights modularity and scalability, following design patterns like Factory, Builder,
Strategy, Command.

## Package Structure

### 1.`bank` package

Contains essential components of the banking system:

- **`Bank`**: Central class that manages users, accounts, cards etc.
- **`User`**: Represents the users of the banking system and implements user-related operations 
  (e.g. adding accounts, cards, transactions).
- **`Commerciant`**: Handles commerciants involved in transactions.
- **`ExchangeRates`**: Provides exchange rate logic for converting between currencies.

#### 1.1`accounts` package

Contains different types of accounts classes and a factory to create them:

- **`Account`**: Base class that defines core functionalities.
- **`ClassicAccount`**: Represents a standard bank account without support for interest rate 
  (this will generate an error). This class extends the Account class.
- **`SavingsAccount`**: Supports everything an Account does, plus interest rate calculations.
- **`AccountFactory`**: Implements the Factory design pattern to create different account types (e.g., ClassicAccount, SavingsAccount).

#### 1.2`cards` package

Contains classes for types of cards:

- **`Card`**: Base class representing a classic bank card.
- **`CardOneTimeUse`**: Represents disposable one-time-use cards that extends the Card class.

### 2.`commands` package

Defines commands that can be made, such as:

- **`AddAccountCommand`**: Adds a new account to the bank database.
- **`AddFundsCommand`**: Deposits funds into an account.
- **`PayOnlineCommand`**: Executes online payments using cards.
- **`SendMoneyCommand`**: Facilitates transfers between accounts.
- **`DeleteAccountCommand`**: Removes an account from the bank database.
- etc.

### 3.`reports` package

Handles the generation of various reports:

- **`ReportFactory`**: Creates different types of reports based on command input.
- **`ReportStrategy`**: Interface for implementing various report generation strategies.  
Implemented by:
  - **`ClassicReport`**: Generates a standard report.
  - **`SpendingReport`**: Generates a spending report.

### 4.`transactions` package

This package handles transactions and how are they built using builder pattern.

- **`Transaction`**: Represents a transaction, storing details about it.  
Contains class:
  - **`TransactionBuilder`**: Implements the Builder pattern to create `Transaction` objects,
    setting various properties like transaction amount, involved accounts, currency etc.

### 5.`utils` package

Contains helper classes:

- **`Utils`**: Provides general-purpose helper methods.
- **`JsonOutput`**: Makes JSON output for different commands.

### 6.`main` package

- **`Main`**: The entry point of the project that initializes the bank, processes commands 
  and triggers the flow of the program.

## Code Flow 

Starting from the `Main` class, the program initializes the `Bank` object, then processes 
commands from input. Each command is then created and using the `execute` method, as I use 
command pattern, commands can be performed. The bank object is used to store all the databases 
for users, accounts, cards, exchange rates etc. Every command interacts with the bank object 
which than retrieves the necessary data and interacts with the other classes in the system 
(e.g. `Account`, `Transaction`, `Card` etc.). Reports are generated using the `ReportFactory`, 
utilizing strategy implementations like `ClassicReport` or `SpendingReport`. Transactions are 
generated using the `TransactionBuilder` and stored by users and accounts.

#### Key Design Patterns:

- **Command Pattern**: Used to encapsulate and execute various commands (e.g., adding accounts, 
  sending money, generating reports, paying online, etc.).
- **Builder Pattern**: Used to create `Transaction` objects, setting various properties 
  like transaction amount, involved accounts, currency etc.
- **Factory Pattern**: The `AccountFactory` creates specific account types (e.g. 
  `ClassicAccount`, `SavingsAccount`) based on user input.
- **Strategy Pattern**: Used in report generation, providing flexibility to format and generate different reports.

## Feedback

Developing this project provided experience with OOP concepts and design patterns.
The modular implementation facilitated the ability to extend the code in 
the future. As and example, managing various transaction types and how they are made can be 
expanded as I use a builder pattern and new attributes can be added. Moreover, I can add more 
types of accounts as I use a factory pattern to create them.

Overall, the project was a great opportunity to apply OOP principles to create a scalable, 
maintainable banking system.