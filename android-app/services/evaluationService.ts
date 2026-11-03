const API_BASE_URL = 'http://localhost:8080';

// Represents one row returned by our Java backend
export interface TruthTableRow {
  variables: Record<string, boolean>;
  result: boolean;
}

// Sends a Boolean expression to the Spring Boot backend 
export async function evaluateExpression(
  expression: string
): Promise<TruthTableRow[]> {
  const url = `${API_BASE_URL}/api/evaluate`;

  let response: Response;
  try {
    response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ expression }),
    });
  } catch (e) {
    throw new Error(`Cannot reach backend at ${url} (${String(e)})`);
  }

  const text = await response.text();
  let data: any = null;
  try {
    data = JSON.parse(text);
  } catch {}

  if (!response.ok) {
    throw new Error(data?.error ?? `Server returned ${response.status}`);
  }

  return data as TruthTableRow[];
}