export type CatMood = "HAPPY" | "HUNGRY" | "PLAYFUL";

export type AdoptionStatus = "IN_SHELTER" | "ADOPTED";

export type Cat = {
    id: number;
    name: string;
    friendship: number;
    mood: CatMood;
    adoptionStatus: AdoptionStatus;
    canBeAdopted: boolean;
    feedCooldownSeconds: number;
    petCooldownSeconds: number;
    playCooldownSeconds: number;
};