# calculator.py
def add(a, b):
    return a + b


def subtract(a, b):
    return a - b


def multiply(a, b):
    return a * b


def divide(a, b):
    if b == 0:
        raise ValueError("除数不能为零")
    return a / b


def calculator():
    print("简易计算器")
    print("支持操作: + (加), - (减), * (乘), / (除)")

    while True:
        try:
            num1 = float(input("请输入第一个数字: "))
            op = input("请输入操作符 (+, -, *, /): ")
            num2 = float(input("请输入第二个数字: "))

            if op == "+":
                result = add(num1, num2)
            elif op == "-":
                result = subtract(num1, num2)
            elif op == "*":
                result = multiply(num1, num2)
            elif op == "/":
                result = divide(num1, num2)
            else:
                print("无效的操作符，请重试！")
                continue

            print(f"结果: {num1} {op} {num2} = {result}")

            cont = input("是否继续计算? (y/n): ").lower()
            if cont != "y":
                print("感谢使用，再见！")
                break
        except ValueError as e:
            print(f"错误: {e}，请输入有效的数字。")
        except Exception as e:
            print(f"发生未知错误: {e}")


if __name__ == "__main__":
    calculator()
