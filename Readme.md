# Advent of Code 2020

## Project Structure

Similar to [last year's attempt](https://github.com/rschmukler/advent-2019) I 
will be using Clojure.

The project follows a familiar structure with the `src/` directory being used 
for source code, the `test/` directory being used for tests, and `resources/` 
will be used for any input files provided.

## Libraries

Like last year, I may reach for libraries either because I enjoy solving problems
with them, or because I'd like to play with them.

On radar for this year:

### Logic
- [core.logic](https://github.com/clojure/core.logic) Logic programming
- [odoyle-rules](https://github.com/oakes/odoyle-rules) Rules engine

### Concurrency
- [manifold](https://github.com/aleph-io/manifold) Stream programming
- [cloroutine](https://github.com/leonoel/cloroutine) Coroutine abstraction
- [core.async](https://github.com/clojure/core.async) `go` like macros + queues

### Misc / Utility
- [core.match](https://github.com/clojure/core.match) Pattern matching
- [wing](https://github.com/teknql/wing) Utility functions written by yours truly
- [datascript](https://github.com/tonsky/datascript) In-memory datomic-like DB

