"""
Extract interview-relevant content from "The Art of Multiprocessor Programming"

Focus: Key concepts, definitions, and interview-worthy patterns
"""

import subprocess
import sys
import re

try:
    import fitz
except ImportError:
    subprocess.check_call([sys.executable, "-m", "pip", "install", "PyMuPDF", "-q"])
    import fitz

PDF_PATH = r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\The Art of Multiprocessor Programming-Morgan Kaufmann (2020).pdf"
OUTPUT_PATH = r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\interview_concepts_extracted.txt"

# Practitioner's track page ranges (approximate from TOC)
CHAPTERS_TO_SCAN = {
    1: (21, 38, "Introduction, Amdahl's Law"),
    2: (39, 65, "Mutual Exclusion - 2.1-2.4, 2.7, 2.9"),
    7: (160, 193, "Spin Locks - skip 7.7-7.9"),
    8: (196, 210, "Monitors - skip 8.5"),
    9: (214, 241, "Linked Lists, Locking Strategies"),
    10: (242, 261, "Queues, ABA Problem"),
    11: (264, 274, "Stacks - 11.1-11.2 only"),
    13: (317, 346, "Concurrent Hashing"),
    14: (347, 368, "Skiplists"),
    16: (388, 411, "Scheduling - skip 16.5"),
    18: (441, 453, "Barriers - 18.1-18.3"),
}

# Keywords that indicate interview-relevant content
INTERVIEW_KEYWORDS = [
    "deadlock", "livelock", "starvation", "race condition",
    "atomic", "CAS", "compare-and-swap", "compareAndSet",
    "ABA problem", "hazard pointer",
    "spin lock", "spinlock", "busy-wait",
    "linearizable", "linearizability", "sequential consistency",
    "lock-free", "wait-free", "obstruction-free",
    "Amdahl", "speedup", "parallelism",
    "fair", "fairness", "starvation-free",
    "backoff", "exponential backoff",
    "CLH", "MCS", "TAS", "TTAS", "test-and-set",
    "memory model", "visibility", "happens-before",
    "volatile", "synchronized",
    "producer-consumer", "readers-writers",
]

def extract_text(doc, start_page, end_page):
    """Extract text from page range"""
    text = []
    for page_num in range(start_page - 1, min(end_page, len(doc))):
        page = doc[page_num]
        text.append(page.get_text())
    return "\n".join(text)

def find_definitions(text):
    """Find definitions and key concepts"""
    lines = text.split("\n")
    definitions = []
    
    for i, line in enumerate(lines):
        line_lower = line.lower()
        # Look for definition patterns
        if any(keyword in line_lower for keyword in INTERVIEW_KEYWORDS):
            # Get context (line + 2 lines after)
            context = "\n".join(lines[max(0,i-1):min(len(lines),i+4)])
            if len(context.strip()) > 50:  # Skip very short matches
                definitions.append(context)
    
    return definitions

def summarize_chapter(doc, ch_num, start, end, description):
    """Extract key interview content from a chapter"""
    text = extract_text(doc, start, end)
    
    # Count keyword occurrences
    keyword_counts = {}
    text_lower = text.lower()
    for keyword in INTERVIEW_KEYWORDS:
        count = text_lower.count(keyword.lower())
        if count > 0:
            keyword_counts[keyword] = count
    
    # Find key definitions
    definitions = find_definitions(text)
    
    return {
        "chapter": ch_num,
        "pages": f"{start}-{end}",
        "description": description,
        "keywords": sorted(keyword_counts.items(), key=lambda x: -x[1])[:10],
        "sample_definitions": definitions[:5],  # Top 5
        "text_preview": text[:2000]  # First 2000 chars
    }

def main():
    print(f"Opening: {PDF_PATH}")
    doc = fitz.open(PDF_PATH)
    print(f"Pages: {len(doc)}")
    
    output = []
    output.append("=" * 80)
    output.append("INTERVIEW-RELEVANT CONCEPTS FROM THE ART OF MULTIPROCESSOR PROGRAMMING")
    output.append("=" * 80)
    output.append("")
    
    all_keywords = {}
    
    for ch_num, (start, end, desc) in CHAPTERS_TO_SCAN.items():
        print(f"Scanning Chapter {ch_num}: {desc}...")
        result = summarize_chapter(doc, ch_num, start, end, desc)
        
        output.append(f"\n{'='*60}")
        output.append(f"CHAPTER {ch_num}: {desc}")
        output.append(f"Pages: {result['pages']}")
        output.append("="*60)
        
        output.append("\n📌 KEY INTERVIEW TERMS FOUND:")
        for keyword, count in result['keywords'][:8]:
            output.append(f"  • {keyword}: {count} occurrences")
            all_keywords[keyword] = all_keywords.get(keyword, 0) + count
        
        if result['sample_definitions']:
            output.append("\n📖 SAMPLE EXCERPTS:")
            for i, defn in enumerate(result['sample_definitions'][:3], 1):
                # Clean up the text
                clean = defn.encode('ascii', 'ignore').decode('ascii').strip()
                clean = re.sub(r'\s+', ' ', clean)[:300]
                output.append(f"\n  [{i}] {clean}...")
        
        output.append(f"\n📄 CHAPTER PREVIEW:")
        preview = result['text_preview'].encode('ascii', 'ignore').decode('ascii')
        preview = re.sub(r'\s+', ' ', preview)[:800]
        output.append(f"  {preview}...")
    
    # Summary
    output.append("\n\n" + "="*80)
    output.append("OVERALL INTERVIEW KEYWORD FREQUENCY")
    output.append("="*80)
    for keyword, count in sorted(all_keywords.items(), key=lambda x: -x[1])[:20]:
        output.append(f"  • {keyword}: {count}")
    
    # Write output
    result_text = "\n".join(output)
    with open(OUTPUT_PATH, "w", encoding="utf-8") as f:
        f.write(result_text)
    
    print(f"\n[OK] Extracted to: {OUTPUT_PATH}")
    print(f"Total characters: {len(result_text)}")
    
    doc.close()

if __name__ == "__main__":
    main()
