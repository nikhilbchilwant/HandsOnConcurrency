import os

files = [
    "./module1-foundations/src/main/java/com/concurrency/labs/lab01/AtomicCounter.java",
    "./module1-foundations/src/main/java/com/concurrency/labs/lab01/SynchronizedCounter.java",
    "./module1-foundations/src/main/java/com/concurrency/labs/lab01/UnsafeCounter.java",
    "./module1-foundations/src/main/java/com/concurrency/labs/lab02/VisibilityBug.java",
    "./module1-foundations/src/main/java/com/concurrency/labs/lab02/VolatileFix.java"
]

for file in files:
    with open(file, 'r') as f:
        content = f.read()

    # manual replacements
    if "AtomicCounter.java" in file:
        content = content.replace("count.incrementAndGet();", "throw new UnsupportedOperationException(\"TODO\");")
        content = content.replace("return count.get();", "throw new UnsupportedOperationException(\"TODO\");")
    if "SynchronizedCounter.java" in file:
        content = content.replace("synchronized(lock){\n            count++;\n        }", "throw new UnsupportedOperationException(\"TODO\");")
        content = content.replace("synchronized(lock){\n            return count;\n        }", "throw new UnsupportedOperationException(\"TODO\");")
        content = content.replace("count++;", "throw new UnsupportedOperationException(\"TODO\");")
        content = content.replace("return count;", "throw new UnsupportedOperationException(\"TODO\");")
    if "UnsafeCounter.java" in file:
        content = content.replace("count++;", "throw new UnsupportedOperationException(\"TODO\");")
        content = content.replace("return count;", "throw new UnsupportedOperationException(\"TODO\");")
    if "VisibilityBug.java" in file:
        if "throw new UnsupportedOperationException" not in content:
            content = content.replace("new Thread(() -> {", "throw new UnsupportedOperationException(\"TODO\");\n/*\nnew Thread(() -> {")
            content = content.replace("        }).start();", "        }).start();\n*/")
            content = content.replace("running = false;", "throw new UnsupportedOperationException(\"TODO\");")
            content = content.replace("return running;", "throw new UnsupportedOperationException(\"TODO\");")
    if "VolatileFix.java" in file:
        if "throw new UnsupportedOperationException" not in content:
            content = content.replace("new Thread(() -> {", "throw new UnsupportedOperationException(\"TODO\");\n/*\nnew Thread(() -> {")
            content = content.replace("        }).start();", "        }).start();\n*/")
            content = content.replace("running = false;", "throw new UnsupportedOperationException(\"TODO\");")
            content = content.replace("return running;", "throw new UnsupportedOperationException(\"TODO\");")

    with open(file, 'w') as f:
        f.write(content)
