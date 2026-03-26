# Propagation Engine

## Overview

This project is a **simplified simulation of a propagation system**, designed to model how a change to one person's value can ripple across a network of related individuals and accounts.

The goal is to demonstrate the **core challenges of real-time recalculation, dependency management, and cross-account impact**, without introducing unnecessary domain complexity.

---

## Problem Statement

In many systems, a single individual (account owner) may:

- Have a **primary value** based on their own data
- Influence other individuals through **relationships**
- Be influenced by other individuals as well
- Have outcomes that depend on multiple sources

When a **value changes for one person**, it can impact:

1. Their own calculated result
2. Values derived by others from them
3. Those individuals’ own outcomes
4. Additional downstream relationships

This creates a **dependency network**, not a simple hierarchy.

---

## What This Project Simulates

This project models a simplified version of that system using:

### Core Concepts

- **Person**: An individual with a base value
- **Relationship**: A connection where one person contributes to another’s derived value
- **Result**: The computed value a person ends up with
- **Propagation**: The process of recalculating impacted individuals after a change

---

## Simplified Rules

To keep the model focused, the simulation uses the following rules:

1. Each person has a **base (own) value**
2. A person may receive a **derived value** from another person (e.g., 50% of their value)
3. If a person has both:
   - an own value  
   - a derived value  
   they use the **higher of the two**
4. If a person’s final value changes, it may impact others who depend on them

---

## Example Scenario

```
Alice (value = 2000)
  ↓ (50%)
Bob (value = 900)
  ↓ (50%)
Charlie (value = 300)
```

### Initial State

- Alice: 2000 (own)
- Bob: max(900, 1000) → 1000 (derived from Alice)
- Charlie: 50% of Bob → 500

### After Alice’s Value Changes to 1400

- Alice: 1400
- Bob: max(900, 700) → 900 (switches to own)
- Charlie: 50% of Bob → 450

### Key Insight

A change to **Alice** affects:
- Bob’s derived value
- Bob’s decision outcome
- Charlie’s value (indirectly)

This demonstrates **multi-level propagation across relationships**.

---

## Why This Matters

This simulation highlights several important system design challenges:

### 1. Dependency Awareness
You must know **who depends on whom** to correctly propagate changes.

### 2. Recalculation Order
Upstream changes must be processed before downstream ones.

### 3. Cross-Relationship Effects
A person can be affected through **multiple connections**, not just one.

### 4. Real-Time Complexity
Immediate recalculation becomes harder as the network grows.

### 5. State Transitions
A person may switch between:
- own value
- derived value

This affects others who depend on them.

---

## Implementation Approach

The project includes two approaches:

### 1. Full Recalculation (Baseline)
Recalculate all individuals every time a change occurs.
- Simple
- Not scalable
- Useful for validation

### 2. Dependency-Based Propagation
Only recalculate impacted individuals using a graph traversal approach:
- Start from the changed person
- Identify directly affected individuals
- Recalculate
- Continue propagation if results change

---

## Goals of This Project

- Demonstrate **propagation mechanics in a network**
- Illustrate why **dependency tracking is critical**
- Show the difference between:
  - brute-force recalculation
  - targeted propagation
- Provide a **clear model for discussion and explanation**
- Serve as a foundation for exploring:
  - real-time vs batch processing
  - system design tradeoffs
  - scalability concerns

---

## Not in Scope

This is intentionally a **simplified model**. It does not include:

- Real-world policy or business rules
- Time-based logic or historical tracking
- Payment or scheduling systems
- Data persistence
- Error handling and retry mechanisms
- Full auditing or traceability

These would be required in a production system.

---

## How to Use

1. Define a set of people and their base values
2. Define relationships between people
3. Run the calculation
4. Change a person’s value
5. Observe how the change propagates through the network

---

## Key Takeaway

This project demonstrates that:

> Systems with interconnected relationships behave like **networks of dependent calculations**, and changes must be handled through **controlled propagation**, not simple updates.
