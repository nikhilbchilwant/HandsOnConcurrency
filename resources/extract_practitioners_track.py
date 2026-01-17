"""
Extract Practitioner's Track sections from "The Art of Multiprocessor Programming"

Practitioner's Track:
- Chapter 1: Full (Amdahl's law emphasis)
- Chapter 2: Sections 2.1-2.4, 2.7, mention 2.9 impossibility proofs
- Chapter 3: Skip 3.5, 3.6
- Chapter 7: Skip 7.7, 7.8, 7.9
- Chapter 8: Full except 8.5 (semaphores)
- Chapters 9, 10: Full except 10.7
- Chapter 11: Only 11.1, 11.2
- Skip Chapter 12
- Chapters 13, 14: Full
- Skip Chapter 15
- Chapter 16: Full except 16.5
- Chapter 17: Optional
- Chapter 18: Sections 18.1-18.3
- Skip Chapter 19 (C++ focused)
- Chapter 20: Optional
"""

import subprocess
import sys

# Install PyMuPDF if not present
try:
    import fitz  # PyMuPDF
except ImportError:
    subprocess.check_call([sys.executable, "-m", "pip", "install", "PyMuPDF", "-q"])
    import fitz

PDF_PATH = r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\The Art of Multiprocessor Programming-Morgan Kaufmann (2020).pdf"
OUTPUT_PATH = r"c:\Users\Nikhil\AntigravityWorkspace\Concurrency\resources\practitioners_track_extracted.txt"

# Define the practitioner's track chapters and sections to include
PRACTITIONERS_TRACK = {
    1: {"include_all": True, "emphasis": "Amdahl's law"},
    2: {"sections": ["2.1", "2.2", "2.3", "2.4", "2.7"], "mention": "2.9 impossibility proofs"},
    3: {"skip_sections": ["3.5", "3.6"]},
    7: {"skip_sections": ["7.7", "7.8", "7.9"]},
    8: {"skip_sections": ["8.5"]},
    9: {"include_all": True},
    10: {"skip_sections": ["10.7"]},
    11: {"sections": ["11.1", "11.2"]},
    # Skip 12
    13: {"include_all": True},
    14: {"include_all": True},
    # Skip 15
    16: {"skip_sections": ["16.5"]},
    # 17 optional
    18: {"sections": ["18.1", "18.2", "18.3"]},
}

def extract_toc(doc):
    """Extract table of contents from PDF"""
    toc = doc.get_toc()
    return toc

def find_chapter_pages(toc):
    """Map chapter numbers to page ranges"""
    chapters = {}
    for i, entry in enumerate(toc):
        level, title, page = entry
        if level == 1 and "CHAPTER" in title.upper():
            # Extract chapter number
            import re
            match = re.search(r'(\d+)', title)
            if match:
                ch_num = int(match.group(1))
                # Find end page (next chapter or end of book)
                end_page = toc[i+1][2] - 1 if i+1 < len(toc) else None
                chapters[ch_num] = {"title": title, "start": page, "end": end_page}
    return chapters

def extract_chapter_text(doc, start_page, end_page):
    """Extract text from a page range"""
    text = []
    for page_num in range(start_page - 1, end_page if end_page else len(doc)):
        page = doc[page_num]
        text.append(f"\n--- Page {page_num + 1} ---\n")
        text.append(page.get_text())
    return "".join(text)

def main():
    print(f"Opening PDF: {PDF_PATH}")
    doc = fitz.open(PDF_PATH)
    print(f"Total pages: {len(doc)}")
    
    # Get TOC
    toc = extract_toc(doc)
    print(f"\nTable of Contents ({len(toc)} entries):")
    
    # Print first-level TOC entries (chapters)
    for level, title, page in toc:
        if level <= 2:
            indent = "  " * (level - 1)
            title_clean = title.encode('ascii', 'ignore').decode('ascii')
            print(f"{indent}{title_clean} (p.{page})")
    
    chapters = find_chapter_pages(toc)
    
    # Extract practitioner's track
    output = []
    output.append("=" * 80)
    output.append("THE ART OF MULTIPROCESSOR PROGRAMMING - PRACTITIONER'S TRACK")
    output.append("=" * 80)
    output.append("")
    
    for ch_num, config in PRACTITIONERS_TRACK.items():
        if ch_num in chapters:
            ch_info = chapters[ch_num]
            output.append(f"\n{'='*60}")
            output.append(f"CHAPTER {ch_num}: {ch_info['title']}")
            if config.get("emphasis"):
                output.append(f"[EMPHASIS: {config['emphasis']}]")
            if config.get("sections"):
                output.append(f"[SECTIONS: {', '.join(config['sections'])}]")
            if config.get("skip_sections"):
                output.append(f"[SKIP: {', '.join(config['skip_sections'])}]")
            if config.get("mention"):
                output.append(f"[MENTION: {config['mention']}]")
            output.append("=" * 60)
            
            # Extract text (first 5 pages of chapter for summary)
            start = ch_info['start']
            end = min(ch_info['start'] + 10, ch_info['end'] or len(doc))
            text = extract_chapter_text(doc, start, end)
            output.append(text[:8000])  # Limit output per chapter
            output.append("\n[... truncated for brevity ...]")
    
    # Write output
    result = "\n".join(output)
    with open(OUTPUT_PATH, "w", encoding="utf-8") as f:
        f.write(result)
    
    print(f"\n\nExtracted to: {OUTPUT_PATH}")
    print(f"Total characters: {len(result)}")
    
    doc.close()
    return result

if __name__ == "__main__":
    main()
