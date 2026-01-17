import sys
print("DEBUG: Simple script start", flush=True)
try:
    import fitz
    print(f"DEBUG: fitz imported version {fitz.__version__}", flush=True)
    with open("c:/Users/Nikhil/AntigravityWorkspace/Concurrency/resources/test_write.txt", "w") as f:
        f.write("Scan successful")
    print("DEBUG: File written", flush=True)
except Exception as e:
    print(f"ERROR: {e}", flush=True)
print("DEBUG: Simple script end", flush=True)
