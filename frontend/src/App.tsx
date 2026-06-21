import { useEffect, useState } from "react";
import type { Cat } from "./types";
import type {SyntheticEvent} from "react";
import CatCard from "./components/CatCard";

import "./App.css";

const API_URL = "http://localhost:8080/api/cats";

function App() {
  const [cats, setCats] = useState<Cat[]>([]);
  const [error, setError] = useState<string>("");
  const [newCatName, setNewCatName] = useState("");

  async function fetchCats() {
    try {
      const response = await fetch(API_URL);

      if(!response.ok) {
        throw new Error("Failed to fetch cats");
      }

      const data: Cat[] = await response.json();
      setCats(data);
    } catch{
      setError("Could not load cats");
  }
  }

  async function createCat() {
    try {
      const response = await fetch(API_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: newCatName.trim()
        }),
      });

      if(!response.ok) {
        throw new Error();
      }
      setNewCatName("");

      await fetchCats();
    } catch {
        setError("Could not create cat");
    }
  }

  const handleCreateCat = async (event: SyntheticEvent) => {
    event.preventDefault();
    await createCat();
  };

  async function adoptCat(catId: number) {
    try {
      const response = await fetch(
          `${API_URL}/${catId}/adopt`,
          {
            method: "POST",
          }
      );
      if (!response.ok) {
        throw new Error();
      }

      await fetchCats()
    } catch {
      setError("Could not adopt cat")
    }
  }

  async function interactWithCat(
      catId: number,
      interactionType: string
  ) {
    try {
      const response = await fetch(
          `${API_URL}/${catId}/interactions`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              type: interactionType,
            }),
          }
      );

      if(!response.ok) {
        const error = await response.json();
        throw new Error(error.message);
      }

      await fetchCats();
    } catch (e) {
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  }

  useEffect(() => {
    fetchCats();
  }, []);

  useEffect(() => {
    if (!error) {
      return;
    }
    const timeout = setTimeout(() => {
      setError("");
    }, 5000);

    return () => clearTimeout(timeout);
  }, [error]);

  const shelterCats = cats.filter(
      (cat) => cat.adoptionStatus === "IN_SHELTER"
  );

  const adoptedCats = cats.filter(
      (cat) => cat.adoptionStatus === "ADOPTED"
  );

  return (
      <main>
        <h1>Cat Shelter 🐾</h1>
        <section className={"stats-bar"}>
          <div>🐈 In shelter: {shelterCats.length}</div>
          <div>🏆 Adopted: {adoptedCats.length}</div>
        </section>
        {error && <p>{error}</p>}

        <div className={"layout"}>
        <section className={"shelter-section"}>
          <h2>Available cats</h2>

          <section>
            <form onSubmit={handleCreateCat}>
              <input
                  value={newCatName}
                  onChange={(e) => setNewCatName(e.target.value)}
                  placeholder="Cat name"
              />
              <button type="submit">Admit cat</button>
            </form>
          </section>

          <div className="cat-grid">
            {shelterCats.map((cat) =>(
                <CatCard
                    key={cat.id}
                    cat={cat}
                    onAdopt={adoptCat}
                    onInteract={interactWithCat}
                />
            ))}
          </div>
        </section>

          <aside className={"honor-wall"}>
          <h2>Honor wall</h2>
          <ul>
            {adoptedCats.map((cat) => (
                <li key={cat.id}>
                  😺 {cat.name}</li>
            ))}
          </ul>
          </aside>
        </div>
      </main>
  );
}

export default App;