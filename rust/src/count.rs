use serde_json::Value;

pub fn count_scalar_values(value: &Value) -> i32 {
    match value {
        Value::Null => 1,
        Value::Bool(_) => 1,
        Value::Number(_) => 1,
        Value::String(_) => 1,
        Value::Array(values) => values.iter().map(count_scalar_values).sum::<i32>(),
        Value::Object(map) => map.values().map(count_scalar_values).sum::<i32>(),
    }
}
