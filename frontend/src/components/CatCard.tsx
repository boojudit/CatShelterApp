import type {Cat} from "../types";
import { useEffect, useState } from "react";

type CatCardProps = {
    cat: Cat;
    onAdopt: (catId: number) => void;
    onInteract: (
        catID: number,
        interactionType: string
    ) => void
};

const CAT_EMOJIS = ["😺", "😸", "😻", "😼", "😽", "🙀", "😿", "😹"];

const MOOD_EMOJIS = {
    HAPPY: "😊",
    HUNGRY: "🍽️",
    PLAYFUL: "🎾",
} as const;

function getCatEmoji(id: number) {
    return CAT_EMOJIS[id % CAT_EMOJIS.length];
}

function CatCard({ cat , onAdopt, onInteract}: CatCardProps) {
    const [feedCooldown, setFeedCooldown] = useState(cat.feedCooldownSeconds);
    const [petCooldown, setPetCooldown] = useState(cat.petCooldownSeconds);
    const [playCooldown, setPlayCooldown] = useState(cat.playCooldownSeconds);

    useEffect(() => {
        setFeedCooldown(cat.feedCooldownSeconds);
        setPetCooldown(cat.petCooldownSeconds);
        setPlayCooldown(cat.playCooldownSeconds);
    }, [cat.feedCooldownSeconds, cat.petCooldownSeconds, cat.playCooldownSeconds]);

    useEffect(() => {
        const interval = setInterval(() => {
                setFeedCooldown((current) => Math.max(0, current - 1));
                setPetCooldown((current) => Math.max(0, current - 1));
                setPlayCooldown((current) => Math.max(0, current - 1));
            }, 1000);

        return () => clearInterval(interval);
    }, []);

    return (
        <article className="cat-card">
        <div className="cat-card-header">
        <span className="cat-emoji">
            {getCatEmoji(cat.id)}
    </span>
    <span
    className="mood-emoji"
    title={`Mood: ${cat.mood}`}
    >
    {MOOD_EMOJIS[cat.mood]}
    </span>
    </div>

    <h3>{cat.name}</h3>
    <p>Friendship: {cat.friendship}</p>
            <div className="interaction-buttons">
                <button
                    title={
                        feedCooldown > 0
                        ? `Feed available in ${feedCooldown}s`
                        : "Feed this cat"
                    }
                    disabled={feedCooldown > 0}
                    onClick={() => onInteract(cat.id, "FEED")}
                >
                    🍖
                </button>
                <button
                    title={
                        petCooldown > 0
                        ? `Pet is available in ${petCooldown}s`
                        : "Pet this cat"
                    }
                    disabled={petCooldown > 0}
                    onClick={() => onInteract(cat.id, "PET")}
                >
                    🖐️
                </button>
                <button
                    title={
                        playCooldown > 0
                        ? `Play is available in ${playCooldown}s`
                        : "Play with this cat"
                    }
                    disabled={playCooldown > 0}
                    onClick={() => onInteract(cat.id, "PLAY")}
                >
                    🎾
                </button>
            </div>
    <button
    className={"adopt-button"}
    disabled={!cat.canBeAdopted}
    title={
        cat.canBeAdopted
        ? "Adopt this cat"
            : "This cat needs more friendship before adoption"
    }
    onClick={() => onAdopt(cat.id)}
    >
        Adopt 🏡
    </button>
    </article>
);
}

export default CatCard;