"""
Single-threaded extraction of practitioner's track chapters.
Simple, robust, linear execution.
"""

import sys
import os
from pathlib import Path

# Install check
try:
    import fitz
except ImportError:
    # This shouldn't happen as we installed it previously
    print("Installing PyMuPDF...")
    import subprocess
    subprocess.check_call([sys.executable, "-m", "pip", "install", "PyMuPDF", "-q"])
    import fitz

PDF_PATH = r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\The Art of Multiprocessor Programming-Morgan Kaufmann (2020).pdf"
OUTPUT_DIR = Path(r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\extracted_chapters")

PRACTITIONERS_TRACK = {
    "ch07_spin_locks": (160, 193),
    "ch09_linked_lists": (214, 241),
    "ch10_queues_aba": (242, 261),
    "ch02_mutual_exclusion": (39, 65),
}

def main():
    print(f"Starting extraction...", flush=True)
    
    if not os.path.exists(PDF_PATH):
        print(f"PDF not found!", flush=True)
        return

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    
    doc = fitz.open(PDF_PATH)
    print(f"Opened PDF. Pages: {len(doc)}", flush=True)

    for name, (start, end) in PRACTITIONERS_TRACK.items():
        print(f"Extracting {name} ({start}-{end})...", flush=True)
        text = []
        for p in range(start-1, end):
            text.append(doc[p].get_text())
        
        out_file = OUTPUT_DIR / f"{name}.txt"
        with open(out_file, "w", encoding="utf-8") as f:
            f.write("\n".join(text))
        print(f"  -> Saved {out_file.name} ({len(text)} pages)", flush=True)

    print("Done.", flush=True)

if __name__ == "__main__":
    main()
