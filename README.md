
# Helio JMapping processor

The JMapping is JSON-based language that allows expressing translation rules (mappings) from heterogeneous sources into RDF. In order to import the JMapping processor so Helio is capable of using JMappings the following components must be registered. **In addition, the JMapping processor is able to understand RML mappings**

```json
{
  "source": "https://github.com/helio-ecosystem/helio-processor-jmapping/releases/download/v0.2.0/helio-processor-jmapping-0.2.0.jar",
  "class": "helio.jmapping.processor.JMappingProcessor",
  "type": "PROCESSOR"
}
```

In addition, in order to properly function and for allowing the usage of some functions the next function class must be loaded as a component

```json
{
  "source": "https://github.com/helio-ecosystem/helio-processor-jmapping/releases/download/v0.2.0/helio-processor-jmapping-0.2.0.jar",
  "class": "helio.jmapping.functions.HF",
  "type": "FUNCTION"
}
```
