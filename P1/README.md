# P1: AST Pretty Printer

## Overview

In this assignment, you will implement a **pretty printer** for badlang. A pretty printer takes an Abstract Syntax Tree (AST) and converts it back into human-readable source code with proper formatting, indentation, and syntax.

## Learning Goals

This assignment has two primary learning objectives:

### 1. **Understand the AST Data Structure**
Your first task is to **investigate and understand the AST data structure** of the badlang language. It is your responsibility to:
- **Explore all AST classes** and understand what each one represents
- **Examine the fields and methods** of each AST node type
- **Understand the relationships** between different AST nodes
- **Investigate how the language constructs** are encoded in the AST

Don't just read the class definitions - **experiment with creating AST instances** in the `Main.java` file to see how they work together. This hands-on exploration is crucial for understanding the language's structure.

### 2. **Master the Visitor Pattern**
The AST uses the **Visitor pattern** for traversal and operations. You will:
- **Implement the visitor interfaces** `Expr.Visitor<String>` and `Stmt.Visitor<String>`
- **Understand how the `accept()` method** works to dispatch to the correct visitor method
- **Learn how visitors enable** clean separation of concerns between data structure and operations
- **Practice implementing** all required visitor methods for each AST node type

The visitor pattern is a fundamental design pattern in compiler construction and AST manipulation.

## What is the badlang Language?

The badlang language is a simple, statically-typed programming language with the following features:

- **Types**: `int` and `bool` (no strings!)
- **Variables**: Declaration and assignment
- **Expressions**: Arithmetic, logical, and comparison operations
- **Control Flow**: `if` statements, `while` loops
- **Functions**: Function definitions with parameters and return values
- **Blocks**: A sequence of statements in a block.

## Expected Output Format

Your pretty printer should produce code that looks like this:

```java
// Variable declaration
int x = 42;
bool flag = true;

// Function definition
fun int factorial(int n) {
  if (n <= 1) {
    return 1;
  } else {
    return n * factorial(n - 1);
  }
}

// While loop
while (x > 0) {
  print x;
  x = x - 1;
}

// If statement
if (flag && x == 0) {
  print 1;
} else {
  print 0;
}

// Nested blocks with scoping
{
  int x = 10;
  {
    int y = 20;
    print y;
  }
  print x;
}

```

## AST Structure

The AST is built using the Visitor pattern with the following classes:

### Expression Classes (`Expr`)

1. **`Binary`** - Binary operations like `a + b`, `x < y`
   - `left`: Left operand expression
   - `operator`: The operator (+, -, *, /, ==, !=, <, <=, >, >=, &&, ||)
   - `right`: Right operand expression

2. **`Literal`** - Constant values
   - `value`: The literal value (integer or boolean)

3. **`Unary`** - Unary operations like `!flag`, `-x`
   - `operator`: The operator (!, -)
   - `right`: The operand expression

4. **`Variable`** - Variable references
   - `name`: Variable name

5. **`Call`** - Function calls like `factorial(5)`
   - `callee`: The function expression
   - `arguments`: List of argument expressions

### Statement Classes (`Stmt`)

1. **`Block`** - Compound statements `{ ... }`
   - `statements`: List of statements in the block. This is used to, for example, define the body of a while loop or a function -- or simply as a block with its own scope.

2. **`Expression`** - Expression statements like `x + 1;`
   - `expression`: The expression to evaluate

3. **`Function`** - Function definitions
   - `name`: Function name
   - `returnType`: Return type (INT or BOOL)
   - `params`: List of parameters with names and types
   - `body`: List of statements in the function body

4. **`If`** - Conditional statements
   - `condition`: The condition expression
   - `thenBranch`: Statement to execute if condition is true
   - `elseBranch`: Statement to execute if condition is false (can be null)

5. **`Print`** - Print statements
   - `expression`: Expression to print

6. **`Return`** - Return statements
   - `value`: Expression to return (can be null)

7. **`Var`** - Variable declarations
   - `name`: Variable name
   - `type`: Variable type (INT or BOOL)
   - `initializer`: Initial value expression (can be null)

8. **`Assign`** - Assignment statements
   - `name`: Variable name
   - `value`: Expression to assign

9. **`While`** - While loops
   - `condition`: Loop condition expression
   - `body`: Loop body statement

### Supporting Classes

- **`VarType`** - Enum with values `INT` and `BOOL`
- **`Operator`** - Enum with arithmetic, logical, and comparison operators

## Your Task

Implement a `PrettyPrinter` class that implements both `Expr.Visitor<String>` and `Stmt.Visitor<String>` interfaces. Your pretty printer should:

1. **Handle all AST node types** - Implement all visitor methods
2. **Produce readable output** - Use proper spacing and indentation
3. **Follow language conventions** - Use appropriate syntax for each construct
4. **Handle edge cases** - Deal with null values and empty lists gracefully

**Important**: Before you start implementing, take time to **explore the AST structure**. Open each AST class file and understand:
- What fields each class contains
- How the classes relate to each other
- What the `accept()` method does
- How the visitor pattern works

This understanding is essential for implementing the pretty printer correctly.

### Testing

Test your pretty printer extensively with various AST structures. Add your tests to `Main.java`:
- Simple expressions: `1 + 2 * 3`
- Nested blocks: `{ int x = 1; { int y = 2; } }`
- Complex functions with multiple parameters
- Nested control flow structures

### Bonus Challenge

**Extra Credit (20 points)**: Extend your pretty printer to output Python syntax instead of badlang syntax. This involves:

- Converting `int`/`bool` types to Python conventions
- Adapting function syntax to Python's `def` keyword
- Converting type declarations to Python's dynamic typing
- Adjusting operator precedence and syntax as needed

Example Python output:
```python
def factorial(n):
    if n <= 1:
        return 1
    else:
        return n * factorial(n - 1)

x = 42
while x > 0:
    print(x)
    x = x - 1
```

## Building and Testing

You can work with it in several ways (Java 17+):

### Option 1: Using an IDE (Recommended)
Open the `badlang` folder in your favorite Java IDE:
- **VS Code**: Install the "Extension Pack for Java" and open the `badlang` folder
- **Cursor**: Same as VS Code - install Java extensions and open the `badlang` folder  
- **IntelliJ IDEA**: Open the `badlang` folder as a project
- **Eclipse**: Import the `badlang` folder as a Maven project

The IDE will automatically recognize the Maven project structure and provide:
- Syntax highlighting and error detection
- Auto-completion for AST classes
- Integrated terminal for running Maven commands
- Debugging capabilities

### Option 2: Command Line
If you prefer the command line:

```bash
# Navigate to the badlang directory
cd badlang

# Build the project
mvn compile

# Run the example program to see all AST examples
mvn exec:java -Dexec.mainClass="edu.wisc.Main"
```

The `Main.java` file contains comprehensive examples of all AST node types:
- **Arithmetic expressions** (binary operations, nested expressions)
- **Variable declarations and assignments** (int, bool types)
- **Control flow** (if statements, while loops)
- **Function definitions and calls** (recursive factorial example)
- **Complex nested structures** (blocks with multiple statements)
- **Logical expressions** (AND, OR, NOT operations)

When you run the program, you'll see that all output is currently empty because the `PrettyPrinter` methods are not yet implemented. This gives you a clear understanding of what needs to be done!

## Submission

Submit your `PrettyPrinter.java` file and `Main.java` file (add more tests). Make sure it compiles and implements all required visitor methods. Include any additional files if you attempt the bonus challenge.

## Grading

- **Basic Implementation (80 points)**: Correctly implements all visitor methods and produces readable output
- **Code Quality (20 points)**: Clean, well-structured code with proper error handling and testing
- **Bonus (20 points)**: Python syntax output (optional)

Good luck!
