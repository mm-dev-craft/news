import React, {useEffect, useRef, useState} from "react";
import {useStore} from "../store.ts";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {
    Button,
    Group,
    Loader,
    Paper,
    Text,
    ScrollArea
} from "@mantine/core";
import {IconArrowLeft} from "@tabler/icons-react";
import {SimiliarHeadlineData} from "./similiar-headline-data.ts";
import {SimilarHeadline} from "./similar-headline";

const SimiliarHeadlinesList: React.FC = () => {
    const headlineIdToCompareAgainst = useStore(
        (state) => state.similiarHeadlinesList.id
    );
    const [result, setResult] = useState<SimiliarHeadlineData[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const abortControllerRef = useRef<AbortController>(new AbortController());

    useEffect(() => {
        const controller = new AbortController();
        abortControllerRef.current = controller;
        axios
            .post<SimiliarHeadlineData[]>(
                `/api/news/${headlineIdToCompareAgainst}/compare`,
                {},
                {signal: controller.signal}
            )
            .then((response) => {
                setResult(response.data);
                setLoading(false);
            })
            .catch((err) => {
                if (err.name === "CanceledError") {
                    console.log("Request cancelled");
                } else {
                    console.error(err);
                    setError("Failed to load data");
                }
                setLoading(false);
            });
    }, [headlineIdToCompareAgainst]);

    const handleBack = () => {
        abortControllerRef.current.abort();
        navigate("/");
    };

    return (
        <Paper p="md">
            <Group mb="md">
                <Button
                    variant="subtle"
                    leftIcon={<IconArrowLeft size={30}/>}
                    onClick={handleBack}
                >
                    Back
                </Button>
            </Group>
            {loading ? (
                <Loader variant="dots"/>
            ) : error ? (
                <Text color="red">{error}</Text>
            ) : (
                <ScrollArea h={700}>
                    {result.map((data: SimiliarHeadlineData) => (
                        <SimilarHeadline
                            key={data.id}
                            title={data.title}
                            link={data.link}
                            summary={data.summary}
                            similarityScore={data.similarityScore}
                            source={data.source}
                        />
                    ))}
                </ScrollArea>
            )}
        </Paper>
    );
};

export default SimiliarHeadlinesList;