# Library-Induced Semantic Spaces (LISS)

This repository contains the implementation of the Library-Induced Semantic Spaces (LISS) approach described in the paper "Searching for Programmatic Policies in Semantic Spaces" by Rubens O. Moraes and Levi H. S. Lelis.

## Overview

LISS is a novel approach to synthesizing programmatic policies for solving Markov Decision Processes (MDPs). Instead of searching in traditional syntax spaces, LISS searches in an approximation of the language's semantic space, resulting in more sample-efficient learning.

### Key Concepts

- **Programmatic Policies**: Policies encoded as programs in a domain-specific language (DSL)
- **Semantic Space**: A search space where neighbors differ in behavior rather than just syntax
- **Library of Programs**: A collection of semantically different programs used to induce the semantic space

## Why LISS?

Traditional syntax-guided synthesis often struggles because:
1. Small syntax changes often don't result in different agent behaviors
2. Search algorithms spend computational budget evaluating semantically identical programs
3. Lack of search guidance can force restarts even when near promising solutions

LISS addresses these issues by:
- Ensuring neighbor programs exhibit different behaviors
- Making search more sample-efficient
- Providing better guidance for local search algorithms


## Requirements

- Java 8 or higher (We tested with Java 17)
- MicroRTS environment
- additional libraries are placed in the `lib` folder (https://github.com/rubensolv/Library-Induced-Semantic-Spaces/tree/main/lib)

## Installation

```bash
# Clone the repository
git clone https://github.com/rubensolv/Library-Induced-Semantic-Spaces.git
cd Library-Induced-Semantic-Spaces

# get external jars files as dependencies
https://github.com/rubensolv/Library-Induced-Semantic-Spaces/tree/main/lib
```

## Usage

All the code in folder  runners (https://github.com/rubensolv/Library-Induced-Semantic-Spaces/tree/main/src/ai/synthesis/dreamcoder/runners) is used to run the experiments. The main classes are:
- `DreamDicV1.java`: Main class to run the LISS algorithm
- `DreamDicV1MultipleMaps.java`: Main class to run the LISS algorithm with multiple maps
- `DreamDicV2MultipleMapsNoFileInputs.java`: Main class to run the LISS algorithm with multiple maps and without any programmatic input
- `MainLoopDreamCoder.java`: The full implementation of the LISS algorithm for robustness maps evolution and iterations.

In folder Runners, you can find 4 main folders:
- `SpaceCount`: Contains the experiment to calculate the number of programs in the semantic space, Experiment 1: pβ in paper.
- `test1`: Contains the experiment to text the usage of IBR instead of the LISS algorithm.
- `test2`: Contains two experiments: One using a group of programs to induce the semantic space with IBR and another starting with no semantic space and only a single program.
- `test3`: Contains two experiments: One using a group of programs to induce the semantic space with LISS and another starting with no semantic space and only a single program. This semantic space is fully random generated.


## Experiments

The paper evaluates LISS on the MicroRTS domain, a real-time strategy game. The experiments demonstrate:

1. LISS is β-proper (β=0.02) compared to syntax space (β=0.19)
2. Local search algorithms are more sample-efficient in LISS than in syntax space
3. LISS outperforms winners of the last three MicroRTS competitions


## Maps Used in Experiments

- **Training**: basesWorkersA (24×24)
- **Testing**:
  - NoWhereToRun (NWR 9×8)
  - itsNotSafe (INS 15×14)
  - letMeOut (LMO 16×8)
  - Barricades (BRR 24×24)
  - Chambers (CHB 32×32)
  - BloodBath.scmB (BBB 64×64)

## Citation

If you use this code in your research, please cite our paper:

```bibtex
@inproceedings{moraes2024searching,
  title={Searching for Programmatic Policies in Semantic Spaces},
  author={Moraes, Rubens O. and Lelis, Levi H. S.},
  booktitle={Proceedings of the Thirty-Third International Joint Conference on Artificial Intelligence (IJCAI-24)},
  pages={5990--5998},
  year={2024}
}
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributors

- [Rubens O. Moraes](https://github.com/rubensolv)
- [Levi H. S. Lelis](https://github.com/levilelis)

## Acknowledgments

This research was supported by Canada's NSERC, the CIFAR AI Chairs program, and Brazil's CAPES. The computational resources were provided by the Digital Research Alliance of Canada and the UFV Cluster.
