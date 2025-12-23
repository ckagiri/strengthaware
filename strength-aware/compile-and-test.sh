#!/bin/bash

# Compilation and Testing Script for Strength-Aware Score Generator
# Run this script to compile, test, and run the demo

echo "=========================================="
echo "Strength-Aware Score Generator"
echo "Compilation and Testing Script"
echo "=========================================="
echo ""

# Check if Java is installed
if ! command -v javac &> /dev/null; then
    echo "❌ Java compiler (javac) not found. Please install JDK 11 or higher."
    echo ""
    echo "Installation instructions:"
    echo "  Ubuntu/Debian: sudo apt-get install openjdk-11-jdk"
    echo "  MacOS: brew install openjdk@11"
    echo "  Windows: Download from https://adoptium.net/"
    exit 1
fi

echo "✓ Java compiler found: $(javac -version 2>&1)"
echo ""

# Create output directory structure
echo "Creating package structure..."
mkdir -p com/ligitabl/scoring
cp *.java com/ligitabl/scoring/
echo "✓ Package structure created"
echo ""

# Compile
echo "Compiling Java files..."
javac com/ligitabl/scoring/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
else
    echo "❌ Compilation failed"
    exit 1
fi
echo ""

# Run tests
echo "=========================================="
echo "Running Validation Tests"
echo "=========================================="
echo ""
java com.ligitabl.scoring.ScoreGeneratorTest
echo ""

# Run demo
echo "=========================================="
echo "Running Premier League Demo"
echo "=========================================="
echo ""
java com.ligitabl.scoring.PremierLeagueDemo
echo ""

echo "=========================================="
echo "All tasks completed successfully!"
echo "=========================================="
echo ""
echo "The compiled .class files are in: com/ligitabl/scoring/"
echo ""
echo "To use in your project:"
echo "  1. Copy the .java files to your source directory"
echo "  2. Ensure package structure matches: com.ligitabl.scoring"
echo "  3. Import classes as needed"
echo ""
