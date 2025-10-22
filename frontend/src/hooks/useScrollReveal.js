import { useEffect, useRef, useState } from "react";

export const useScrollReveal = (direction = "left", threshold = 0.2, delay = 0) => {
    const ref = useRef(null);
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setTimeout(() => setVisible(true), delay); // apply stagger delay
                    observer.unobserve(entry.target); // trigger only once
                }
            },
            { threshold }
        );

        if (ref.current) observer.observe(ref.current);

        return () => {
            if (ref.current) observer.unobserve(ref.current);
        };
    }, [threshold, delay]);

    const style = {
        opacity: visible ? 1 : 0,
        transform: visible
            ? "translateX(0)"
            : direction === "left"
                ? "translateX(-50px)"
                : "translateX(50px)",
        transition: `opacity 0.8s ease-out, transform 0.8s ease-out`,
    };

    return [ref, style];
};
