"""
Multithreaded extraction of practitioner's track chapters from
"The Art of Multiprocessor Programming"

Extracts full text from each chapter in parallel using ThreadPoolExecutor.
"""

import subprocess
import sys
import re
import os
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

try:
    import fitz
except ImportError:
    subprocess.check_call([sys.executable, "-m", "pip", "install", "PyMuPDF", "-q"])
    import fitz

PDF_PATH = r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\The Art of Multiprocessor Programming-Morgan Kaufmann (2020).pdf"
OUTPUT_DIR = Path(r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\extracted_chapters")

# Practitioner's track chapters with page ranges
PRACTITIONERS_TRACK = {
    "ch01_introduction": (21, 38, "Introduction, Amdahl's Law"),
    "ch02_mutual_exclusion": (39, 65, "Mutual Exclusion (2.1-2.4, 2.7, 2.9)"),
    "ch03_concurrent_objects": (66, 91, "Concurrent Objects (skip 3.5, 3.6)"),
    "ch07_spin_locks": (160, 193, "Spin Locks and Contention (skip 7.7-7.9)"),
    "ch08_monitors": (196, 210, "Monitors and Blocking (skip 8.5)"),
    "ch09_linked_lists": (214, 241, "Linked Lists: Role of Locking"),
    "ch10_queues_aba": (242, 261, "Queues, Memory Management, ABA"),
    "ch11_stacks": (264, 274, "Stacks (11.1-11.2 only)"),
    "ch13_hashing": (317, 346, "Concurrent Hashing"),
    "ch14_skiplists": (347, 368, "Skiplists and Balanced Search"),
    "ch16_scheduling": (388, 411, "Scheduling and Work Distribution"),
    "ch18_barriers": (441, 453, "Barriers (18.1-18.3)"),
}

def extract_chapter(pdf_path: str, chapter_id: str, start_page: int, end_page: int, description: str) -> tuple:
    """Extract text from a single chapter (runs in thread)"""
    try:
        # Each thread opens its own PDF handle
        doc = fitz.open(pdf_path)
        
        text_parts = []
        text_parts.append(f"{'='*80}")
        text_parts.append(f"CHAPTER: {description}")
        text_parts.append(f"Pages: {start_page}-{end_page}")
        text_parts.append(f"{'='*80}\n")
        
        for page_num in range(start_page - 1, min(end_page, len(doc))):
            page = doc[page_num]
            page_text = page.get_text()
            # Clean up text
            page_text = page_text.replace('\ufb01', 'fi').replace('\ufb02', 'fl')
            text_parts.append(f"\n--- Page {page_num + 1} ---\n")
            text_parts.append(page_text)
        
        doc.close()
        
        full_text = "\n".join(text_parts)
        return (chapter_id, description, full_text, None)
        
    except Exception as e:
        return (chapter_id, description, None, str(e))

def main():
    import sys
    print(f"DEBUG: Starting main...", flush=True)
    print(f"PDF: {PDF_PATH}", flush=True)
    print(f"Output: {OUTPUT_DIR}", flush=True)
    
    # Create output directory
    try:
        OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
        print(f"DEBUG: Created output dir {OUTPUT_DIR}", flush=True)
    except Exception as e:
        print(f"ERROR creating dir: {e}", flush=True)
        return
    
    # Verify PDF exists
    if not os.path.exists(PDF_PATH):
        print(f"ERROR: PDF not found at {PDF_PATH}")
        return
    
    print(f"\nExtracting {len(PRACTITIONERS_TRACK)} chapters using ThreadPoolExecutor...")
    print("-" * 60)
    
    results = {}
    
    # Use ThreadPoolExecutor for parallel extraction
    with ThreadPoolExecutor(max_workers=4) as executor:
        # Submit all extraction tasks
        futures = {
            executor.submit(
                extract_chapter, 
                PDF_PATH, 
                chapter_id, 
                start, 
                end, 
                desc
            ): chapter_id 
            for chapter_id, (start, end, desc) in PRACTITIONERS_TRACK.items()
        }
        
        # Collect results as they complete
        for future in as_completed(futures):
            chapter_id = futures[future]
            try:
                ch_id, description, text, error = future.result()
                if error:
                    print(f"  [FAIL] {ch_id}: {error}")
                else:
                    # Save to file
                    output_file = OUTPUT_DIR / f"{ch_id}.txt"
                    with open(output_file, "w", encoding="utf-8") as f:
                        f.write(text)
                    
                    char_count = len(text)
                    print(f"  [OK] {ch_id}: {char_count:,} chars -> {output_file.name}")
                    results[ch_id] = {
                        "description": description,
                        "chars": char_count,
                        "file": str(output_file)
                    }
            except Exception as e:
                print(f"  [ERROR] {chapter_id}: {e}")
    
    # Create summary file
    summary_path = OUTPUT_DIR / "_summary.txt"
    with open(summary_path, "w", encoding="utf-8") as f:
        f.write("PRACTITIONER'S TRACK - EXTRACTED CHAPTERS\n")
        f.write("="*60 + "\n\n")
        
        total_chars = 0
        for ch_id, info in sorted(results.items()):
            f.write(f"{ch_id}:\n")
            f.write(f"  Description: {info['description']}\n")
            f.write(f"  Characters: {info['chars']:,}\n")
            f.write(f"  File: {info['file']}\n\n")
            total_chars += info['chars']
        
        f.write(f"\nTOTAL: {len(results)} chapters, {total_chars:,} characters\n")
    
    print("-" * 60)
    print(f"\n[DONE] Extracted {len(results)} chapters")
    print(f"Total: {sum(r['chars'] for r in results.values()):,} characters")
    print(f"Summary: {summary_path}")

if __name__ == "__main__":
    main()
