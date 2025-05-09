import { ITermin } from "../api/termin/addTermin";

/**
 * function that generates both free and taken slots (termini) from an array of taken ones
 * @param ITermin[] aktivni termini
 */
export function generateTermini(termini: ITermin[]): [ITermin?, ITermin?][] {
  const out: [ITermin?, ITermin?][] = [];

  for (let i = 0; i < 8; i++) {
    out[i] = [undefined, undefined];
  }

  for (let termin of termini) {
    // first washer queue
    const ind = termin.Termin;
    out[ind][(termin.Washer % 2) - 1] = termin;
  }

  return out;
}
